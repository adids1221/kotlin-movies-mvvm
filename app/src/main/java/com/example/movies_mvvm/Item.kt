package com.example.movies_mvvm

data class Item(
    val title: String,
    val description: String,
    val releaseDate: String,
    val rating: Double?,
    val poster: String?
)

object MovieItemManager {
    val items: MutableList<Item> = mutableListOf()

    fun add(movieItem: Item) {
        items.add(movieItem)
    }

    fun remove(position: Int) {
        items.removeAt(position)
    }
}

val imagePrefix = "https://image.tmdb.org/t/p/w500/"

val demoData =
    arrayListOf(
        Item(
            "Cocaine Bear",
            "Inspired by a true story, an oddball group of cops, criminals, tourists and teens converge in a Georgia forest where a 500-pound black bear goes on a murderous rampage after unintentionally ingesting cocaine.",
            "2023-02-22",
            6.6,
            imagePrefix + "gOnmaxHo0412UVr1QM5Nekv1xPi.jpg",
        ),
        Item(
            "John Wick: Chapter 4",
            "With the price on his head ever increasing, John Wick uncovers a path to defeating The High Table. But before he can earn his freedom, Wick must face off against a new enemy with powerful alliances across the globe and forces that turn old friends into foes.",
            "2023-03-22",
            8.2,
            imagePrefix + "vZloFAK7NmvMGKE7VkF5UHaz0I.jpg",
        ),
        Item(
            "Knock at the Cabin",
            "\"While vacationing at a remote cabin, a young girl and her two fathers are taken hostage by four armed strangers who demand that the family make an unthinkable choice to avert the apocalypse. With limited access to the outside world, the family must decide what they believe before all is lost.",
            "2023-02-01",
            6.4,
            imagePrefix + "dm06L9pxDOL9jNSK4Cb6y139rrG.jpg",
        ),
        Item(
            "Shotgun Wedding",
            "Darcy and Tom gather their families for the ultimate destination wedding but when the entire party is taken hostage, “’Til Death Do Us Part” takes on a whole new meaning in this hilarious, adrenaline-fueled adventure as Darcy and Tom must save their loved ones—if they don’t kill each other first.",
            "2022-12-28",
            6.3,
            imagePrefix + "t79ozwWnwekO0ADIzsFP1E5SkvR.jpg",

            ),
        Item(
            "A Man Called Otto",
            "When a lively young family moves in next door, grumpy widower Otto Anderson meets his match in a quick-witted, pregnant woman named Marisol, leading to an unlikely friendship that turns his world upside down.",
            "2022-12-28",
            7.9,
            imagePrefix + "130H1gap9lFfiTF9iDrqNIkFvC9.jpg",
        ),
        Item(
            "The Pope's Exorcist",
            "Father Gabriele Amorth, Chief Exorcist of the Vatican, investigates a young boy's terrifying possession and ends up uncovering a centuries-old conspiracy the Vatican has desperately tried to keep hidden.",
            "2023-04-05",
            0.0,
            imagePrefix + "4IjRR2OW2itjQWQnmlUzvwLz9DQ.jpg",
        ),
        Item(
            "Dungeons & Dragons: Honor Among Thieves",
            "A charming thief and a band of unlikely adventurers undertake an epic heist to retrieve a lost relic, but things go dangerously awry when they run afoul of the wrong people.",
            "2023-03-24",
            7.9,
            imagePrefix + "6LuXaihVIoJ5FeSiFb7CZMtU7du.jpg",
        ),
        Item(
            "Missing",
            "When her mother disappears while on vacation in Colombia with her new boyfriend, June’s search for answers is hindered by international red tape. Stuck thousands of miles away in Los Angeles, June creatively uses all the latest technology at her fingertips to try and find her before it’s too late. But as she digs deeper, her digital sleuthing raises more questions than answers... and when June unravels secrets about her mom, she discovers that she never really knew her at all.",
            "2023-01-19",
            7.7,
            imagePrefix + "jYXJW19PMEUrpyavEHrnnlItQn1.jpg",
        ),
        Item(
            "Close",
            "Two thirteen year-olds have always been incredibly close but they drift apart after the intimacy of their relationship is questioned by schoolmates.  An emotionally transformative and unforgettable portrait of the intersection of friendship and love, identity and independence, and heartbreak and healing.",
            "2023-02-16",
            7.8,
            imagePrefix + "nTtKyaaeQhcwiGg8sYcr3XyPdQZ.jpg",
        ),
    )