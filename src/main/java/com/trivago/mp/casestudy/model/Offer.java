package com.trivago.mp.casestudy.model;

import java.util.Objects;

/**
 * A concrete offer from a specific advertiser
 */
public class Offer {
    private final Advertiser advertiser;
    private final int priceInEuro;
    private final int cpc;

    public Offer(Advertiser advertiser, int priceInEuro, int cpc) {
        this.advertiser = advertiser;
        this.priceInEuro = priceInEuro;
        this.cpc = cpc;
    }

    public Advertiser getAdvertiser() {
        return advertiser;
    }

    public int getPriceInEuro() {
        return priceInEuro;
    }

    /**
     * the cost per click an advertiser pays for a particular offer
     */
    public int getCpc() {
        return cpc;
    }

    @Override
    public String toString() {
        return "Offer{" + "advertiser=" + advertiser + ", priceInEuro=" + priceInEuro + ", cpc=" + cpc + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return priceInEuro == offer.priceInEuro &&
                cpc == offer.cpc &&
                Objects.equals(advertiser, offer.advertiser);
    }

    @Override
    public int hashCode() {

        return Objects.hash(advertiser, priceInEuro, cpc);
    }
}
