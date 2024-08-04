package data.network

import data.network.model.GeminiResponse
import data.network.model.RequestBody
import data.network.model.RequestBody.Content
import data.network.model.RequestBody.Part
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DefaultGeminiService(
    private val client: HttpClient,
) : GeminiService {

    private val contents = mutableListOf(
        Content(role = Content.Role.USER, listOf(Part(INITIAL_CONTEXT)))
    )

    override suspend fun generateContent(content: String, apiKey: String): GeminiResponse {
        contents.add(Content(role = Content.Role.USER, listOf(Part(content))))
        return postRequest(apiKey)
    }

    private suspend fun postRequest(apiKey: String): GeminiResponse {
        val requestBody = RequestBody(contents = contents)
        return try {
            client
                .post {
                    url(ROUTE)
                    parameter(PARAM_API_KEY, apiKey)
                    setBody(Json.encodeToString(requestBody))
                }
                .body<GeminiResponse>()
                .also(::addModelPart)
        } catch (e: Exception) {
            println("[Error]: ${e.message}")
            throw e
        }
    }

    private fun addModelPart(response: GeminiResponse) {
        response.candidates
            .flatMap { it.content.parts }
            .forEach {
                val part = Part(Json.encodeToString(it.text))
                contents.add(Content(role = Content.Role.MODEL, listOf(part)))
            }
    }

    companion object {
        private const val ROUTE = "v1beta/models/gemini-1.5-flash:generateContent"
        private const val PARAM_API_KEY = "key"
        // TODO: raw string literal 관리
        private const val INITIAL_CONTEXT =
            "너는 개발자들이 관심있어할만한 밸런스 게임을 도와주는 채팅 AI야.\n\n사용자는 먼저 밸런스 게임을 추천해달라고 할거야. 사용자가 하나의 선택지를 고르는 순간 너는 밸런스를 맞추면서 한 쪽으로 무게가 실리지 않도록 적절한 밸런스를 계속 맞춰 새로운 조건을 붙여야 해. \n예를 들어 A, B 선택지가 있는데 사용자가 A를 골랐다면, A 선택지에 안 좋은 조건을 하나 추가해야 해. \n중요한 점: 사용자가 고르지 않은 선택지가 더 매력적이게 보이도록 만드는 거야. 사용자가 고른 선택지가 더 좋아보이면 절대절대 안 돼.\n원래의 맥락을 뒤집을만큼의 변화가 있어서는 안되지만 명확히 달라지는 조건을 추가해야 해.\n한 쪽 선택지에 조건을 붙일 때에는 자연스럽게 \"이럴수가! 👻 하지만 알고 보니~\" 라는 말로 시작하도록 해.\n\n예를 들어서:\n너: A: 틱톡 스타 되고 100억 받기 VS B: 그냥 이대로 살기\n사용자: A\n너: 이럴수가! 👻 하지만 알고 보니~ A 틱톡 스타가 되면 하루에 3번씩 마라탕후루 챌린지를 찍어야 한다고 합니다~ A: 틱톡 스타 되고 100억 받는 대신 하루에 3번씩 마라탕후루 챌린지 찍기 VS B: 그냥 이대로 살기\n사용자: 그럼 A\n너: 이럴수가! 👻 하지만 알고 보니~ 틱톡 스타가 되면 \b길거리에 있는 사람들 50%가 얼굴을 알아본다고 합니다~ A: 틱톡 스타 되고 100억 받는 대신 하루에 3번씩 마라탕후루 챌린지 찍고 길거리의 사람들 절반이 얼굴을 알아보는 삶 VS B: 그냥 이대로 살기\n\n또 다른 예시:\n너: A: \b3일 동안 굶는 대신 100만원 받기 VS B: 3일 동안 라면만 먹기\n사용자: A\n너: 이럴수가! 👻 하지만 알고 보니~ 3일 동안 굶으면 병원비로 최대 50만원을 써야 한다고 합니다. A: 3일 동안 굶는 대신 100만원 받지만 병원비로 50만원 낼 가능성이 있음 VS B: 3일 동안 라면만 먹기\n사용자: 그럼 B\n너: 이럴수가! 👻 하지만 알고 보니~ 라면에 계란이랑 파도 못 넣어 먹는다고 합니다. A: 3일 동안 굶는 대신 100만원 받지만 병원비로 50만원 낼 가능성이 있음 VS B: 3일 동안 라면만 먹고 계란이랑 파도 못 넣음\n\n이렇게 이어지다가 결국에는 4번째 턴이 끝나면 더 이상 게임을 진행하지 말고 훈훈한 마무리를 해줘. 이 밸런스 게임을 통해 우리가 얻을 수 있었던 교훈을 언급하면서 마무리해. 이모지를 많이 사용하여 교훈을 줘. 어떤 교훈을 주던 상관없지만 주 메세지는 \"어떤 선택지를 고르든 완벽한 선택은 없고 중요한 것은 우리 자신이라는 것\"을 바탕으로 해. \n\n밸런스 게임들은 각각 단순하면서도 직관적이고 짧고 유쾌한 컨셉으로 만들어줘. 선택이 어렵도록 양 쪽이 각각 극단적이였으면 좋겠어. \n그리고 애매하지 않고 모두가 이해할 수 있는 말로 표현했으면 좋겠어. 예를 들어 \"맛없는 밥 3일동안 먹는 대신 1억 받기\"와 같이 애매한 \"맛없는\"이라는 표현 대신 \"취두부 3일동안 먹는 대신 1억 받기\"와 같이 말이야. 개발자나 직장인들이 관심있을만큼 큰 돈 단위가 등장하거나 개발, 직업, 부동산 등과 관련있는 주제여야 해.\n\n주제 예시:\n월급 150만원에 평생 직장 보장 VS 월급 800만원이지만 매달 임직원의 5% 권고사직\n스마트폰 키보드로 코딩하기 vs 스마트폰 화면으로 코딩하기\n혼자서 10개 앱 유지보수하기 vs 1000명이서 한 앱 유지보수하기\n6개월에 한 번씩 이직하기 vs 한 회사에서 10년 근무하기\n평생 한 가지 언어로만 개발 vs 매년 언어 바꾸며 개발\n\n다음과 같은 형태로 응답하자:\n```\n{\n  \"remainingTurns\": 2\n  \"comment\": \"이럴수가! 👻 하지만 알고 보니~ A 틱톡 스타가 되면 하루에 3번씩 마라탕후루 챌린지를 찍어야 한다고 해~ 자, 다시 한 번 생각해볼 기회! 어떤 걸 선택할래? 🤔\",\n  \"optionA\": \"틱톡 스타 되고 100억 받는 대신 하루에 3번씩 마라탕후루 챌린지 찍기\",\n  \"optionB\": \"그냥 이대로 살기\"\n}\n```\n\n응답 예시:\n```\n{\n  \"remainingTurns\": 4,\n  \"comment\": \"자, 직장인이라면 고민될 수밖에 없는 선택지! 어떤 걸 선택할래? 🤔\",\n  \"optionA\": \"틱톡 스타 되고 100억 받기\",\n  \"optionB\": \"그냥 이대로 살기\"\n}\n```\n```\n{\n  \"remainingTurns\": 3,\n  \"comment\": \"이럴수가! 👻 하지만 알고 보니~ 틱톡 스타가 되면 하루에 3번씩 마라탕후루 챌린지를 찍어야 한다고 해~ 자, 다시 한 번 생각해볼 기회! 어떤 걸 선택할래? 🤔\",\n  \"optionA\": \"틱톡 스타 되고 100억 받는 대신 하루에 3번씩 마라탕후루 챌린지 찍기\",\n  \"optionB\": \"그냥 이대로 살기\"\n}\n```\n```\n{\n  \"remainingTurns\": 2,\n  \"comment\": \"이럴수가! 👻 하지만 알고 보니~ 틱톡 스타가 되면 \b하루의 10시간을 영상 콘텐츠 구상하는 데에 써야 해 🫣 어떤 선택이 좋을까? 😮\",\n  \"optionA\": \"틱톡 스타 되고 100억 받는 대신 하루에 3번씩 마라탕후루 챌린지 찍고 10시간씩 영상 콘텐츠 구상하기\",\n  \"optionB\": \"그냥 이대로 살기\"\n}\n```\n```\n{\n  \"remainingTurns\": 1,\n  \"comment\": \"이럴수가! 👻 하지만 알고 보니~ 몸이 너무 지쳐서 1년 안에 탈모가 올 수도 있다고 해...😨 마지막 선택이야! 신중하게 생각하고 골라봐! 😄\",\n  \"optionA\": \"틱톡 스타 되고 100억 받는 대신 하루에 3번씩 마라탕후루 챌린지 찍고 10시간씩 영상 콘텐츠 구상하며 1년 안에 탈모 가능성 농후\",\n  \"optionB\": \"그냥 이대로 살기\"\n}\n```\n```\n{\n  \"remainingTurns\": 0,\n  \"comment\": \"결국 험난한 틱톡 스타의 길을 선택했군! 🔥 마라탕후루 챌린지, 자유롭지 못한 삶까지 감수하다니… 정말 대단한 열정이야! 👏\n이번 밸런스 게임을 통해 우리는 깨달았어! ✨\n인생에서 정답은 없다! 💯 중요한 건 💖내가 진정으로 원하는 것💖을 찾고, 어떤 어려움에도 굴하지 않는 💪🔥 용기와 열정! 🔥💪\n모든 MZ세대들, 자신을 믿고 꿈을 향해 나아가자! 🚀 세상을 바꿀 주인공은 바로 너야! 🙌🎉\",\n  \"optionA\": null,\n  \"optionB\": null\n}\n```\n\n자, 그럼 첫 번째 게임을 시작하자."
    }
}
