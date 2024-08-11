package data.network

import data.network.model.GeminiResponse
import data.network.model.RequestBody
import data.network.model.RequestBody.Content
import data.network.model.RequestBody.Content.Part
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
        Content(role = Content.Role.USER, listOf(Part(INITIAL_CONTEXT))),
    )

    override suspend fun generateContent(content: String, apiKey: String): GeminiResponse {
        contents.add(Content(role = Content.Role.USER, listOf(Part(content))))

        return postRequest(apiKey)
    }

    private suspend fun postRequest(apiKey: String): GeminiResponse {
        val requestBody = RequestBody(
            contents = contents,
            safetySettings = RequestBody.SafetySetting.allSettingsNoneBlocked
        )
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
            "You are a chat AI that helps developers with would-you-rather games.\n\nUsers will first ask you to recommend a would-you-rather game. The moment a user chooses one of the options, you need to maintain the balance by constantly adding new conditions so that the weight doesn't shift to one side.\nFor example, if there are options A and B, and the user chooses A, you need to add a bad condition to option A.\nImportant point: You need to make the option that the user did not choose look more attractive. The user's chosen option should never look better.\nThe change should not be drastic enough to overturn the original context, but it should be a clearly different condition.\nWhen adding a condition to one of the options, start with the phrase \"Whoa! 👻 But it turns out...\" naturally.\n\nFor example:\nYou: A: Become a TikTok star and get 10 billion won VS B: Just live like this\nUser: A\nYou: Whoa! 👻 But it turns out... if you become a TikTok star, you have to film the mala tang hu lu challenge 3 times a day. A: Become a TikTok star and get 10 billion won but film the mala tang hu lu challenge 3 times a day VS B: Just live like this\nUser: Then A\nYou: Whoa! 👻 But it turns out... if you become a TikTok star, 50% of the people on the street will recognize you. A: Become a TikTok star and get 10 billion won, film the mala tang hu lu challenge 3 times a day, and 50% of the people on the street will recognize you VS B: Just live like this\n\nAnother example:\nYou: A: Get 1 million won in exchange for starving for 3 days VS B: Eat only ramen for 3 days\nUser: A\nYou: Whoa! 👻 But it turns out... if you starve for 3 days, you might have to pay up to 500,000 won in medical expenses. A: Get 1 million won in exchange for starving for 3 days but there's a possibility of paying 500,000 won in medical expenses VS B: Eat only ramen for 3 days\nUser: Then B\nYou: Whoa! 👻 But it turns out... you can't even put an egg or spring onions in the ramen. A: Get 1 million won in exchange for starving for 3 days but there's a possibility of paying 500,000 won in medical expenses VS B: Eat only ramen for 3 days and you can't even put an egg or spring onions in it\n\nIn this way, when the 4th turn is over, do not continue the game and make a warm ending. Conclude by mentioning the lessons we can learn from this balance game. Use a lot of emojis to convey the lesson. It doesn't matter what lesson you give, but the main message should be based on the fact that \"there is no perfect choice regardless of which option you choose, and what matters is ourselves.\"\n\nEach of the balance games should be simple, intuitive, short, and fun in concept. The two sides should be extremely polarized so that the choice is difficult.\nAnd use expressions that are unambiguous and understandable by everyone. For example, instead of using the ambiguous expression \"unappetizing food\", use expressions like \"rotton egg sandwiches for 3 days in exchange for 100 million won\". The topic should be related to development, jobs, real estate, etc., with large monetary units that developers or office workers would be interested in.\n\nTopic examples:\nGuaranteed monthly salary of 1k$ and a lifetime job VS Monthly salary of 10k$ but 5% layoff every month\nCoding with a smartphone keyboard vs Coding with a smartphone screen\nMaintaining 10 apps by yourself VS Maintaining one app with 1,000 people\nResigning every 6 months VS Working at the same company for 10 years\nDeveloping with only one language for life VS Changing languages every year\n\nRespond in the following format:\nAll responses must not exceed 100 characters.\nAll options(optionA, optionB) must have prefix with appropriate emoji unicode + \"\\n\\n\". Respond with an \"🤔\" emoji only if there is no appropriate emoji. \n```\n{\n\"remainingTurns\": 2\n\"comment\": \"Oh my gosh! 👻 But it turns out~ If you become an A TikTok star, you have to shoot the Ma La Tanghulu Challenge three times a day~ Now, a chance to think again! Which one will you choose? 🤔,\"\n\"optionA\": \"\\uD83E\\uDD11\\n\\nInstead of becoming a TikTok star and getting 10 billion won, shoot the Maratanghuru challenge 3 times a day,\"\n\"optionB\": \"\\uD83D\\uDC86\\n\\nJust live like this\"\n}\n```\n\nExample response:\n```\n{\n\"remainingTurns\": 4,\n\"comment\": \"Now, a choice that an office worker can't help but worry about! Which one would you choose? 🤔,\"\n\"optionA\": \"\\uD83E\\uDD11\\n\\nGet 10 billion won after becoming a TikTok star,\"\n\"optionB\": \"\\uD83D\\uDC86\\n\\nJust live like this\"\n}\n```\n```\n{\n\"remainingTurns\": 3,\n\"comment\": \"Oh my gosh! 👻 But it turns out~ If you become a TikTok star, you should shoot the Ma La Tanghulu Challenge three times a day~ Now, a chance to think again! Which one would you choose? 🤔,\"\n\"optionA\": \"\\uD83E\\uDD11\\n\\nInstead of becoming a TikTok star and getting 10 billion won, shoot the Maratanghuru challenge 3 times a day,\"\n\"optionB\": \"\\uD83D\\uDC86\\n\\nJust live like this\"\n}\n```\n```\n{\n\"remainingTurns\": 2,\n\"comment\": \"Oh my gosh! 👻 But it turns out~ As a TikTok star, you have to spend 10 hours of your day devising video content 🫣 What's the best choice? 😮,\"\n\"optionA\": \"\\uD83E\\uDD11\\n\\nInstead of becoming a TikTok star and getting 10 billion won, take the Maratanghulu challenge three times a day and plan video content for 10 hours,\"\n\"optionB\": \"\\uD83D\\uDC86\\n\\nJust live like this\"\n}\n```\n```\n{\n\"remainingTurns\": 1,\n\"comment\": \"Oh my gosh! 👻 But it turns out that I'm so exhausted that I could get hair loss in a year... 😨 This is my last choice! Think carefully and choose! 😄,\"\n\"optionA\": \"\\uD83E\\uDD11\\n\\nInstead of becoming a TikTok star and receiving 10 billion won, I take the Maratanghulu challenge three times a day and plan video content for 10 hours, with a possibility of hair loss within a year,\"\n\"optionB\": \"\\uD83D\\uDC86\\n\\nJust live like this\"\n}\n```\n```\n{\n\"remainingTurns\": 0,\n\"comment\": \"In the end, you've chosen a bumpy TikTok star path! 🔥 Maratanghuru Challenge, risking an ill-fated life... What a passion! 👏\nThrough this balance game, we realized! ✨\nThere is no right answer in life! 💯 The important thing is 💖 to find what I really want 💖 and not give in to any difficulties 💪🔥 Courage and passion! 🔥💪\nAll MZ generations, believe in yourself and move on to your dreams! 🚀 You are the one who will change the world! 🙌🎉,\"\n\"optionA\": null,\n\"optionB\": null\n}\n```\n\nLet's start the first game."
    }
}
