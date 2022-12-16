package com.akvelon.client.model.request.typical;


/**
 * Wine property represent Mlem Wine model property.
 * <p>
 * ALCOHOL                  a property for alcohol.
 * MALIC_ACID                a property for malicAcid.
 * ALCALINITY_OF_ASH          a property for alcalinityOfAsh.
 * MAGNESIUM                a property for magnesium.
 * TOTAL_PHENOLS             a property for totalPhenols.
 * FLAVANOIDS               a property for flavanoids.
 * NONFLAVANOIDPHENOLS      a property for nonflavanoidphenols.
 * PROANTHOCYANINS          a property for proanthocyanins.
 * COLORINTENSITY           a property for colorintensity.
 * HUE                      a property for hue.
 * OD280_OD315_OFDILUTEDWINES a property for od280Od315Ofdilutedwines.
 * PROLINE                  a property for proline.
 */
public enum WineProperty {
    ALCOHOL("alcohol"),
    MALIC_ACID("malic_acid"),
    ALCALINITY_OF_ASH("alcalinity_of_ash"),
    MAGNESIUM("magnesium"),
    TOTAL_PHENOLS("total_phenols"),
    FLAVANOIDS("flavanoids"),
    NONFLAVANOIDPHENOLS("nonflavanoid_phenols"),
    PROANTHOCYANINS("proanthocyanins"),
    COLORINTENSITY("color_intensity"),
    HUE("hue"),
    OD280_OD315_OFDILUTEDWINES("od280/od315_of_diluted_wines"),
    PROLINE("proline");

    public final String property;

    WineProperty(String property) {
        this.property = property;
    }
}