package com.phoenixforge.student.domain.progression

data class GardenMerchantItem(
    val itemId: String,
    val displayName: String,
    val cost: Int,
    val meshHint: String = "",
)

object GardenMerchantCatalog {
    const val MERCHANT_ID = "garden_merchant"

    val items: List<GardenMerchantItem> = listOf(
        GardenMerchantItem("garden.sunflower_01", "Sunflower seed", 3, "sunflower"),
        GardenMerchantItem("garden.watering_can", "Tiny watering can", 8, "watering_can"),
        GardenMerchantItem("garden.gnome_friend", "Garden gnome friend", 12, "gnome"),
        GardenMerchantItem("garden.butterfly_house", "Butterfly house", 15, "butterfly_house"),
    )

    fun find(itemId: String): GardenMerchantItem? = items.firstOrNull { it.itemId == itemId }
}
