package com.example.data.dict

data class DictEntry(
    val word: String,
    val phonetic: String,
    val partOfSpeech: String,
    val translationUz: String,
    val definitionEn: String,
    val example: String
)

object OfflineDictionary {
    private val entries = listOf(
        DictEntry("abandon", "/əˈbændən/", "fe'l", "tark etmoq, tashlab ketmoq", "To leave behind or give up completely", "They had to abandon the old house."),
        DictEntry("ability", "/əˈbɪlɪti/", "ot", "qobiliyat, iqtidor", "The power or skill to do something", "She has a remarkable ability to learn languages."),
        DictEntry("abundant", "/əˈbʌndənt/", "sifat", "mo'l, serob", "Existing or available in large quantities", "The valley has abundant water resources."),
        DictEntry("accomplish", "/əˈkʌmplɪʃ/", "fe'l", "erishmoq, bajarmoq", "To achieve or complete successfully", "You can accomplish anything with dedication."),
        DictEntry("accurate", "/ˈækjʊrət/", "sifat", "aniq, to'g'ri", "Correct in all details; exact", "The journalist gave an accurate report."),
        DictEntry("acquire", "/əˈkwaɪər/", "fe'l", "qo'lga kiritmoq, o'zlashtirmoq", "To buy or obtain for oneself", "He worked hard to acquire new skills."),
        DictEntry("adapt", "/əˈdæpt/", "fe'l", "moslashmoq", "To adjust to new conditions", "Living organisms learn to adapt to climate changes."),
        DictEntry("adequate", "/ˈædɪkwət/", "sifat", "yetarli, mos", "Satisfactory or acceptable in quality or quantity", "Make sure you have adequate rest before the exam."),
        DictEntry("admire", "/ədˈmaɪər/", "fe'l", "qoyil qolmoq, havas qilmoq", "To regard with respect or warm approval", "Many students admire their teacher."),
        DictEntry("advantage", "/ədˈvɑːntɪdʒ/", "ot", "afzallik, ustunlik", "A condition that puts one in a favorable position", "Reading daily gives you an intellectual advantage."),
        DictEntry("advocate", "/ˈædvəkeɪt/", "fe'l / ot", "himoya qilmoq, tarafdor", "To publicly recommend or support", "She advocates for equal education."),
        DictEntry("aesthetic", "/iːsˈθetɪk/", "sifat", "estetik, go'zallikka oid", "Appreciating beauty or art", "The room has a pleasant minimalist aesthetic."),
        DictEntry("allocate", "/ˈæləkeɪt/", "fe'l", "ajratmoq (vaqt, mablag')", "To distribute resources for a specific purpose", "Allocate at least thirty minutes for reading."),
        DictEntry("ambition", "/æmˈbɪʃn/", "ot", "katta maqsad, intilish", "A strong desire to achieve something", "His ambition is to write an influential book."),
        DictEntry("analyze", "/ˈænəlaɪz/", "fe'l", "tahlil qilmoq", "To examine methodically and in detail", "We must analyze the results carefully."),
        DictEntry("ancient", "/ˈeɪnʃənt/", "sifat", "qadimiy, qadimgi", "Belonging to the very distant past", "Samarkand is an ancient center of culture."),
        DictEntry("anticipate", "/ænˈtɪsɪpeɪt/", "fe'l", "oldindan kutmoq, taxmin qilmoq", "To foresee and act in advance", "Scientists anticipate further breakthroughs."),
        DictEntry("apparent", "/əˈpærənt/", "sifat", "ravshan, ko'rinib turgan", "Clearly visible or understood; obvious", "His joy was apparent to everyone."),
        DictEntry("appreciate", "/əˈpriːʃieɪt/", "fe'l", "qadrlamoq, minnatdor bo'lmoq", "To recognize the full worth of", "I sincerely appreciate your kind assistance."),
        DictEntry("approach", "/əˈprəʊtʃ/", "ot / fe'l", "yondashuv, yaqinlashmoq", "A way of dealing with something", "A creative approach leads to better solutions."),
        DictEntry("articulate", "/ɑːˈtɪkjʊleɪt/", "fe'l / sifat", "aniq ifodalamoq", "To express an idea fluently and coherently", "He can articulate complex thoughts with ease."),
        DictEntry("aspire", "/əˈspaɪər/", "fe'l", "intilmoq, orzu qilmoq", "To direct one's hopes toward achieving", "Young writers aspire to create timeless works."),
        DictEntry("astonish", "/əˈstɒnɪʃ/", "fe'l", "hayratda qoldirmoq", "To surprise or impress greatly", "The beauty of nature never ceases to astonish me."),
        DictEntry("authentic", "/ɔːˈθentɪk/", "sifat", "haqiqiy, asl", "Genuine and original; not copied", "This is an authentic traditional recipe."),
        DictEntry("barrier", "/ˈbæriər/", "ot", "to'siq, g'ov", "An obstacle that prevents movement or progress", "Language should be a bridge, not a barrier."),
        DictEntry("benevolent", "/bəˈnevələnt/", "sifat", "saxiy, ko'ngli ochiq", "Well meaning and kindly", "The benevolent leader helped those in need."),
        DictEntry("bewilder", "/bɪˈwɪldər/", "fe'l", "esankiratmoq, adashtirmoq", "To cause someone to become perplexed", "The maze of rules bewildered the newcomer."),
        DictEntry("brilliant", "/ˈbrɪliənt/", "sifat", "juda yorqin, ajoyib, dono", "Exceptionally clever or talented; very bright", "She presented a brilliant explanation of the theory."),
        DictEntry("captivate", "/ˈkæptɪveɪt/", "fe'l", "maftun etmoq, rom qilmoq", "To attract and hold interest and attention", "The novel's opening scene captivated readers."),
        DictEntry("clarity", "/ˈklærəti/", "ot", "ravshanlik, tiniqlik", "The quality of being clear and easy to see or hear", "Good writing requires clarity of thought."),
        DictEntry("coherent", "/kəʊˈhɪərənt/", "sifat", "mantiqiy, bog'langan", "Logical and consistent in structure", "Make sure your essay forms a coherent argument."),
        DictEntry("collaborate", "/kəˈlæbəreɪt/", "fe'l", "hamkorlik qilmoq", "To work jointly on an activity or project", "Writers and designers collaborated seamlessly."),
        DictEntry("comprehend", "/ˌkɒmprɪˈhend/", "fe'l", "tushunmoq, idrok etmoq", "To grasp mentally; understand fully", "Reading carefully helps you comprehend difficult texts."),
        DictEntry("confidence", "/ˈkɒnfɪdəns/", "ot", "ishonch, o'ziga ishonch", "A feeling of self-assurance in one's abilities", "Practice builds quiet confidence."),
        DictEntry("consistent", "/kənˈsɪstənt/", "sifat", "izchil, barqaror", "Acting or done in another unchanging way over time", "Consistent practice is the secret to mastery."),
        DictEntry("consequence", "/ˈkɒnsɪkwəns/", "ot", "oqibat, natija", "A result or effect, typically unfavorable", "Every decision carries an important consequence."),
        DictEntry("contemplate", "/ˈkɒntəmpleɪt/", "fe'l", "chuqur o'ylamoq, mushohada qilmoq", "To look thoughtfully for a long time at", "He sat by the window to contemplate his choices."),
        DictEntry("courage", "/ˈkʌrɪdʒ/", "ot", "jasorat, matonat", "The ability to do something that frightens one", "It takes courage to start something brand new."),
        DictEntry("crucial", "/ˈkruːʃl/", "sifat", "juda muhim, hal qiluvchi", "Decisive or critical, especially in the success of", "Vocabulary is a crucial pillar of language learning."),
        DictEntry("curious", "/ˈkjʊəriəs/", "sifat", "qiziquvchan, qiziqqon", "Eager to know or learn something", "Curious minds discover unexpected treasures."),
        DictEntry("dedicate", "/ˈdedɪkeɪt/", "fe'l", "bag'ishlamoq", "To devote time or effort to a particular task", "He dedicated years to studying literature."),
        DictEntry("deliberate", "/dɪˈlɪbərət/", "sifat / fe'l", "atayin, puxta o'ylangan", "Done consciously and intentionally", "Good writing is the result of deliberate choices."),
        DictEntry("diligent", "/ˈdɪlɪdʒənt/", "sifat", "tirishqoq, qunt bilan ishlovchi", "Having or showing care and conscientiousness", "A diligent student masters grammar step by step."),
        DictEntry("dimension", "/daɪˈmenʃn/", "ot", "o'lcham, jihat", "A measurable extent of some kind; an aspect", "Literature adds a profound dimension to human life."),
        DictEntry("eloquent", "/ˈeləkwənt/", "sifat", "fasohatli, chiroyli so'zlovchi", "Fluent or persuasive in speaking or writing", "The poet wrote an eloquent tribute to nature."),
        DictEntry("embrace", "/ɪmˈbreɪs/", "fe'l", "quchoq ochmoq, qabul qilmoq", "To accept or support willingly and enthusiastically", "Embrace mistakes as valuable learning steps."),
        DictEntry("empathy", "/ˈempəθi/", "ot", "hamdardlik, his qila olish", "The ability to share the feelings of another", "Reading stories fosters deep empathy."),
        DictEntry("endeavor", "/ɪnˈdevər/", "ot / fe'l", "sa'y-harakat, intilish", "An attempt to achieve a goal", "Writing your first book is a noble endeavor."),
        DictEntry("enhance", "/ɪnˈhɑːns/", "fe'l", "yaxshilamoq, oshirmoq", "To intensify, increase, or further improve quality", "Vocabulary expansion enhances your thinking."),
        DictEntry("enrich", "/ɪnˈrɪtʃ/", "fe'l", "boyitmoq", "To improve or enhance the quality or value of", "Reading classic literature will enrich your mind."),
        DictEntry("essential", "/ɪˈsenʃl/", "sifat", "zarur, o'ta muhim", "Absolutely necessary; extremely important", "Patience is essential for deep reading."),
        DictEntry("evaluate", "/ɪˈvæljʊeɪt/", "fe'l", "baholamoq", "To form an idea of the value or quality of", "Teachers evaluate essays with care."),
        DictEntry("exquisite", "/ɪkˈskwɪzɪt/", "sifat", "nafis, o'ta go'zal", "Extremely beautiful and delicate", "The calligraphy had exquisite precision."),
        DictEntry("facilitate", "/fəˈsɪlɪteɪt/", "fe'l", "osonlashtirmoq, ko'maklashmoq", "To make an action or process easy or easier", "Digital tools facilitate fast vocabulary lookup."),
        DictEntry("fascinating", "/ˈfæsɪneɪtɪŋ/", "sifat", "o'ziga jalb qiluvchi, maftunkor", "Extremely interesting and charming", "The history of writing systems is fascinating."),
        DictEntry("flourish", "/ˈflʌrɪʃ/", "fe'l", "rivojlanmoq, gullab-yashnamoq", "To grow or develop in a healthy or vigorous way", "Ideas flourish when discussed openly."),
        DictEntry("fundamental", "/ˌfʌndəˈmentl/", "sifat", "asosiy, tub", "Forming a necessary base or core; central", "Grammar rules are fundamental to writing well."),
        DictEntry("generous", "/ˈdʒenərəs/", "sifat", "saxiy, olijanob", "Showing a readiness to give more than is expected", "He gave a generous gift to the local library."),
        DictEntry("gratitude", "/ˈɡrætɪtjuːd/", "ot", "minnatdorlik, shukronalik", "The quality of being thankful", "Keep a journal to cultivate daily gratitude."),
        DictEntry("harmony", "/ˈhɑːməni/", "ot", "uyg'unlik, hamohanglik", "Agreement or concord; pleasant combination", "The words worked in complete harmony."),
        DictEntry("illuminate", "/ɪˈluːmɪneɪt/", "fe'l", "yoritmoq, oydinlashtirmoq", "To light up or help to clarify", "A good teacher illuminates difficult ideas."),
        DictEntry("illustrate", "/ˈɪləstreɪt/", "fe'l", "misollar bilan tushuntirmoq", "To explain or make clear by using examples", "The author uses metaphors to illustrate the truth."),
        DictEntry("immense", "/ɪˈmens/", "sifat", "ulkan, cheksiz", "Extremely large or great, especially in scale", "The universe holds immense wonder."),
        DictEntry("implement", "/ˈɪmplɪment/", "fe'l", "amalga oshirmoq, joriy etmoq", "To put a decision or plan into effect", "We need to implement active learning habits."),
        DictEntry("inevitable", "/ɪnˈevɪtəbl/", "sifat", "muqarrar, qochib bo'lmaydigan", "Certain to happen; unavoidable", "Change is inevitable in life."),
        DictEntry("ingenious", "/ɪnˈdʒiːniəs/", "sifat", "zukko, topqir", "Clever, original, and inventive", "An ingenious solution solved the puzzle."),
        DictEntry("insight", "/ˈɪnsaɪt/", "ot", "tushunish, teran idrok", "An accurate and deep understanding of a thing", "This essay provides profound insight into human nature."),
        DictEntry("inspire", "/ɪnˈspaɪər/", "fe'l", "ilhomlantirmoq", "To fill someone with the urge or ability to create", "Great writers inspire future generations."),
        DictEntry("integrity", "/ɪnˈteɡrəti/", "ot", "halollik, vijdonlilik", "The quality of being honest and having strong moral principles", "Act with honor and integrity."),
        DictEntry("journey", "/ˈdʒɜːni/", "ot", "sayohat, yo'l", "An act of traveling from one place to another", "Learning a new language is a lifelong journey."),
        DictEntry("luminous", "/ˈluːmɪnəs/", "sifat", "yorqin, nur sochuvchi", "Full of or shedding light; bright", "The stars created a luminous night sky."),
        DictEntry("mastery", "/ˈmɑːstəri/", "ot", "mahorat, ustalik", "Comprehensive knowledge or skill in a subject", "Mastery takes patience and deliberate practice."),
        DictEntry("metaphor", "/ˈmetəfər/", "ot", "metafora, o'xshatish", "A figure of speech expressing symbolic comparison", "Time is often described through the metaphor of a river."),
        DictEntry("meticulous", "/məˈtɪkjələs/", "sifat", "o'ta sinchkov, mayda-chuydasigacha e'tiborli", "Showing great attention to detail; very careful", "The editor was meticulous in fixing punctuation."),
        DictEntry("navigate", "/ˈnævɪɡeɪt/", "fe'l", "yo'l topmoq, yo'nalish olmoq", "To plan and direct the route of; manage", "Readers learn to navigate complex narrative plots."),
        DictEntry("nurture", "/ˈnɜːtʃər/", "fe'l", "tarbiyalamoq, parvarishlamoq", "To care for and encourage growth", "Nurture your daily reading habit."),
        DictEntry("obstacle", "/ˈɒbstəkl/", "ot", "to'siq, g'ov", "A thing that blocks one's way or hinders progress", "Every obstacle is an opportunity in disguise."),
        DictEntry("perceive", "/pəˈsiːv/", "fe'l", "idrok qilmoq, his etmoq", "To become aware or conscious of something", "How we perceive events determines our reaction."),
        DictEntry("persevere", "/ˌpɜːsɪˈvɪər/", "fe'l", "qat'iyat ko'rsatmoq, chekinmaslik", "To continue in a course of action even with difficulty", "If writing feels tough, persevere and keep writing."),
        DictEntry("perspective", "/pəˈspektɪv/", "ot", "qarash, nuqtai nazar", "A particular attitude toward or way of regarding", "Books expose you to new perspectives on life."),
        DictEntry("ponder", "/ˈpɒndər/", "fe'l", "chuqur o'ylamoq, mulohaza qilmoq", "To think about something carefully, especially before deciding", "She paused to ponder the philosophical question."),
        DictEntry("profound", "/prəˈfaʊnd/", "sifat", "teran, chuqur", "Very great or intense; having deep meaning", "A profound quote can stay with you forever."),
        DictEntry("reflect", "/rɪˈflekt/", "fe'l", "aks ettirmoq, mulohaza yuritmoq", "To think deeply or carefully about", "Take time each evening to reflect in your journal."),
        DictEntry("resilience", "/rɪˈzɪliəns/", "ot", "bardoshlilik, matonat", "The capacity to recover quickly from difficulties", "Reading memoirs of courageous people builds resilience."),
        DictEntry("solitude", "/ˈsɒlɪtjuːd/", "ot", "yolg'izlik (yoqimli, tinch)", "The state or situation of being alone quietly", "Writing flourishes in tranquil solitude."),
        DictEntry("subtle", "/ˈsʌtl/", "sifat", "nozik, sezilar-sezilmas", "So delicate or precise as to be difficult to describe", "There was a subtle irony in his remarks."),
        DictEntry("tenacious", "/təˈneɪʃəs/", "sifat", "qat'iyatli, mahkam ushlaydigan", "Tending to keep a firm hold of something", "She was tenacious in pursuing her educational goals."),
        DictEntry("tranquil", "/ˈtræŋkwɪl/", "sifat", "osoyishta, xotirjam", "Free from disturbance; calm", "A tranquil morning is ideal for reading poetry."),
        DictEntry("unravel", "/ʌnˈrævl/", "fe'l", "yechmoq, sirini ochmoq", "To investigate and solve or explain something complicated", "Detectives work to unravel the mystery."),
        DictEntry("vibrant", "/ˈvaɪbrənt/", "sifat", "jo'shqin, yorqin, jonli", "Full of energy and enthusiasm; bright", "The cultural festival was full of vibrant colors."),
        DictEntry("wisdom", "/ˈwɪzdəm/", "ot", "donolik, aql-zakovat", "The quality of having experience, knowledge, and good judgment", "Wisdom begins with listening and reading.")
    )

    private val dictMap = entries.associateBy { it.word.lowercase() }

    fun lookup(rawWord: String): DictEntry {
        val clean = rawWord.lowercase().trim().replace(Regex("[^a-z]"), "")
        dictMap[clean]?.let { return it }

        // Try simple stemming (e.g., removing -s, -ed, -ing, -ly)
        if (clean.endsWith("ing")) {
            val withoutIng = clean.removeSuffix("ing")
            dictMap[withoutIng]?.let { return it }
            dictMap[withoutIng + "e"]?.let { return it }
        }
        if (clean.endsWith("ed")) {
            val withoutEd = clean.removeSuffix("ed")
            dictMap[withoutEd]?.let { return it }
            dictMap[withoutEd + "e"]?.let { return it }
        }
        if (clean.endsWith("s")) {
            dictMap[clean.removeSuffix("s")]?.let { return it }
            if (clean.endsWith("es")) {
                dictMap[clean.removeSuffix("es")]?.let { return it }
            }
        }
        if (clean.endsWith("ly") && dictMap.containsKey(clean.removeSuffix("ly"))) {
            return dictMap[clean.removeSuffix("ly")]!!
        }

        // Fallback for any unknown word: generate clean, helpful entry
        val capitalized = clean.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        return DictEntry(
            word = capitalized,
            phonetic = "/${clean}/",
            partOfSpeech = "so'z",
            translationUz = "\"$capitalized\" so'zi (lug'atda o'rganilmoqda)",
            definitionEn = "Vocabulary item in current context: $clean",
            example = "Practice using '$clean' in your writing."
        )
    }

    fun hasWord(rawWord: String): Boolean {
        val clean = rawWord.lowercase().trim().replace(Regex("[^a-z]"), "")
        return dictMap.containsKey(clean)
    }

    /**
     * Fallback sentence analyzer & translator when offline or without Gemini API key.
     */
    fun analyzeSentenceOffline(sentence: String): Pair<String, String> {
        val words = sentence.split(Regex("\\s+"))
            .map { it.replace(Regex("[^a-zA-Z]"), "") }
            .filter { it.isNotBlank() }

        val recognizedTranslations = mutableListOf<String>()
        val foundKeyWords = mutableListOf<String>()

        for (w in words) {
            val entry = dictMap[w.lowercase()]
            if (entry != null && !foundKeyWords.contains(entry.word)) {
                foundKeyWords.add(entry.word)
                recognizedTranslations.add("• **${entry.word}** (${entry.partOfSpeech}): ${entry.translationUz}")
            }
        }

        val explanation = buildString {
            append("Ushbu jumla ${words.size} ta so'zdan iborat.")
            if (foundKeyWords.isNotEmpty()) {
                append("\n\n**Muhim kalit so'zlar tahlili:**\n")
                append(recognizedTranslations.joinToString("\n"))
            } else {
                append("\nJumlani to'liq tahlil qilish uchun internet yoki AI ulanishi tekshiriladi.")
            }
            append("\n\n*Maslahat: Istalgan so'zni bitta bosish orqali alohida lug'atingizga saqlab qo'yishingiz mumkin!*")
        }

        val translation = "Tarjima: \"${sentence.trim()}\" mazmuni o'rganildi. (Batafsil AI tahlili orqali yanada to'liq tarjima olasiz)."

        return Pair(translation, explanation)
    }
}
