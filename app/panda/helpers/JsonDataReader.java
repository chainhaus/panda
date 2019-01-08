package panda.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import play.api.Play;
import play.libs.Json;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class JsonDataReader {

    public static List<?> getCommonData(String filePath) {
        File file = Play.current().getFile(filePath);
        JsonNode jsonNode;
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerFor(new TypeReference<List<?>>() {
        });
        List<?> list = null;
        try {
            FileInputStream is = new FileInputStream(file);
            jsonNode = Json.parse(is);
            list = reader.readValue(jsonNode);
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
/*
    public static List<Alert> getAllAlertsFromJSON() {
        return (List<Alert>) getCommonData("conf/data-store/alerts/alerts.json");
    }

    public static List<Deals> getAllDealSheets() {
        return (List<Deals>) getCommonData("conf/data-store/back-office/deals.json");
    }

    public static List<Referral> getAllReferrals() {
        return (List<Referral>) getCommonData("conf/data-store/back-office/referrals.json");
    }

    public static List<Market> getAllMarkets() {
        return (List<Market>) getCommonData("conf/data-store/markets/market.json");
    }

    public static List<Awarded> getAllAwarded() {
        return (List<Awarded>) getCommonData("conf/data-store/referrals/awarded.json");
    }

    public static List<Bids> getAllBids() {
        return (List<Bids>) getCommonData("conf/data-store/referrals/bids.json");
    }

    public static List<model.referrals.Deals> getAllDeals() {
        return (List<model.referrals.Deals>) getCommonData("conf/data-store/referrals/deals.json");
    }

    public static List<IndicatedInterest> getAllIndicatedInterests() {
        return (List<IndicatedInterest>) getCommonData("conf/data-store/referrals/indicated_interest.json");
    }

    public static List<Issued> getAllIssued() {
        return (List<Issued>) getCommonData("conf/data-store/referrals/issued.json");
    }*/
}
