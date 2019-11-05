package ca.concordia.app.risk.controller.delegate;

import org.springframework.beans.factory.annotation.Autowired;

import ca.concordia.app.risk.controller.dto.BorderDto;
import ca.concordia.app.risk.controller.dto.ContinentDto;
import ca.concordia.app.risk.controller.dto.CountryDto;
import ca.concordia.app.risk.services.MapService;

/**
 * MapBusinessDelegate The facade class to access the MapService
 * 
 * @see MapService
 * 
 */
public class MapBusinessDelegate {

	/**
	 * Dependency injection from GameService
	 */
	@Autowired
	private MapService mapService;

	/**
	 * This method used for add a continent.
	 * @param continentDto continent DTO
	 */
	public void addContinent(ContinentDto continentDto) {
		mapService.addContinent(continentDto);

	}

	/**
	 * This method used for remove a continent.
	 * @param continentDto continent DTO
	 */
	public void removeContinent(ContinentDto continentDto) {
		mapService.removeContinent(continentDto);
	}

	/**
	 * This method used for add a country.
	 * @param countryDto country DTO
	 */
	public void addCountry(CountryDto countryDto) {
		mapService.addCountry(countryDto);

	}

	/**
	 * This method used for remove a country.
	 * @param countryDto country DTO
	 */
	public void removeCountry(CountryDto countryDto) {
		mapService.removeCountry(countryDto);

	}

	/**
	 * This method used for add a neighbor.
	 * @param borderDto border Dto
	 */
	public void addNeighbor(BorderDto borderDto) {
		mapService.addNeighbor(borderDto);
	}

	/**
	 * This method used for remove neighbor.
	 * @param borderDto border Dto
	 */
	public void removeNeighbor(BorderDto borderDto) {
		mapService.removeNeighbor(borderDto);
	}
}
