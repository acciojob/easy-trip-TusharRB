package Repository;

import com.driver.model.Airport;
import java.util.*;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Repository
public class AirportRepository {

    HashMap<String , Airport> airportDb = new HashMap<>();
    HashMap<Integer, Flight>flightDb=new HashMap<>();

    HashMap<Integer,Passenger>passengerDb=new HashMap<>();
    HashMap<Integer, HashSet<Passenger>> flightPassengerDb=new HashMap<>();

    public String addAirport(Airport airport){

        String key = airport.getAirportName();

        airportDb.put(key,airport);

        return "Airport Added Successfully";
    }
    public String getLargestAirportName(){

        int maxNoOfTerminals=0;
        String largestAirport="";
        for(Airport airport:airportDb.values()){
            if(airport.getNoOfTerminals() > maxNoOfTerminals){
                maxNoOfTerminals= airport.getNoOfTerminals();
                largestAirport=airport.getAirportName();
            }else if(airport.getNoOfTerminals() == maxNoOfTerminals){
                if(airport.getAirportName().compareTo(largestAirport) < 0){
                    largestAirport=airport.getAirportName();
                }
            }
        }

        return largestAirport;
    }

    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity){

        double duration=Integer.MAX_VALUE;
        for(Flight flight:flightDb.values()){
            if(flight.getFromCity() == fromCity && flight.getToCity() == toCity){
                Math.min(duration,duration=flight.getDuration());
            }
        }
        if(duration == Integer.MAX_VALUE)
            return -1;

        return duration;
    }

    public int getNumberOfPeopleOn( Date date, String airportName){
        if(!airportDb.containsKey(airportName))
            return 0;

        Airport airport=airportDb.get(airportName);
        City cityAirport=airport.getCity();

        int count=0;

        for(Flight flight:flightDb.values()){
            Date flightDate=flight.getFlightDate();
            if(flightDate == date && flight.getToCity() == cityAirport )
                count++;
        }



        return count;
    }
    public int calculateFlightFare(Integer flightId){

        Flight flight=flightDb.get(flightId);
        int fare=3000 + flight.getTicketsBooked() * 50 ;


        return fare;
    }


    public String bookATicket(Integer flightId,Integer passengerId){

        if(!(flightPassengerDb.containsKey(flightId)))
            return "Fail";
        Passenger passenger=passengerDb.get(passengerId);
        if(flightPassengerDb.get(flightId).size() > flightDb.get(flightId).getMaxCapacity() || flightPassengerDb.containsValue(passenger))
            return "Fail";


        HashSet<Passenger>passengerList=flightPassengerDb.get(flightId);
        passengerList.add(passengerDb.get(passengerId));

        int ticketsBooked=flightDb.get(flightId).getTicketsBooked();
        flightDb.get(flightId).setTicketsBooked(ticketsBooked+1);

        return "Successful";
    }

    public String cancelATicket(Integer flightId,Integer passengerId){

        HashSet<Passenger>passengerList=flightPassengerDb.get(flightId);
        Passenger passenger=passengerDb.get(passengerId);

        if(!(flightPassengerDb.containsKey(flightId)) || passengerList.contains(passenger)){
            return "Fail";
        }

        passengerList.remove(passenger);

        return "Successful";
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId){

        int count=0;
        for(HashSet<Passenger> passengerList: flightPassengerDb.values()){
            Passenger passenger=passengerDb.get(passengerId);
            if(passengerList.contains(passenger))
                count++;
        }
        return count;
    }

    public String addFlight(Flight flight){

        int key=flight.getFlightId();
        flightDb.put(key,flight);

        return "Successful";
    }

    public String getAirportNameFromFlightId(Integer flightId){

        if(flightDb.containsKey(flightId)) {
            Flight flight = flightDb.get(flightId);
            City airportName = flight.getFromCity();
            return airportName.toString();
        }

        return null;
    }

    public int calculateRevenueOfAFlight(Integer flightId){

        Flight flight=flightDb.get(flightId);
        int tickets=flight.getTicketsBooked();
        int revenue=0;
        for(int i=0 ;i < tickets ; i++){
            revenue+=3000+i*50;
        }


        return revenue;
    }

    public String addPassenger(Passenger passenger){

        //Add a passenger to the database
        //And return a "SUCCESS" message if the passenger has been added successfully.
        int key = passenger.getPassengerId();

        passengerDb.put(key,passenger);

        return "Successful";
    }
}
