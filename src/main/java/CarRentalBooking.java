import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.swagger.client.ApiException;
import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.CarSearchResponse;
import models.carrental.Car;
import models.carrental.CarRentalSearch;
import models.carrental.Result;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CarRentalBooking extends Booking{
    private DefaultApi apiInstance;

    public CarRentalBooking(DefaultApi apiInstance) {
        this.apiInstance = apiInstance;
    }

    @Override
    public void receiveRequest(Estimate estimate, Schedule schedule) {

    }

    @Override
    public List<Object> provideOptions(List<String> list) throws ApiException{
        String location = list.get(0);
        String pickUp = list.get(1);
        String dropOff = list.get(2);
        String lang = null;
        String currency = null;
        String provider = null;
        String rateClass = null;
        String ratePlan = null;
        String rateFilter = null;
        List<String> vehicle = null;

        List<Object> carRentalResults = new ArrayList<>();

        try {
            CarSearchResponse response = apiInstance.carRentalAirportSearch(apiKey,location, pickUp, dropOff, lang, currency, provider, rateClass, ratePlan, rateFilter, vehicle);

            Gson gson = new Gson();
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = gson.toJson(response);

            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray search = jsonObject.getJSONArray("results");

            JSONObject searchResults;
            JSONArray cars;
            JSONObject carResults;

            CarRentalSearch drive = objectMapper.readValue(jsonString, CarRentalSearch.class);
            for (int i = 0; i < search.length(); i++) {
                searchResults = search.getJSONObject(i);
                drive.setResult(objectMapper.readValue(String.valueOf(searchResults), Result.class));
                cars = searchResults.getJSONArray("cars");
                for (int j = 0; j < cars.length(); j++) {
                    carResults = cars.getJSONObject(j);
                    drive.setCar(objectMapper.readValue(String.valueOf(carResults), Car.class));
                    carRentalResults.add(drive);
                }
            }
        }
        catch (ApiException e) {
            throw e;
        }
        catch (IOException e) {

        }
        return carRentalResults;
    }
}
