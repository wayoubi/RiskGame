package ca.concordia.app.risk.model.modes;

import ca.concordia.app.risk.exceptions.RiskGameRuntimeException;
import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.BorderDaoImp;
import ca.concordia.app.risk.model.dao.CountryDaoImpl;
import ca.concordia.app.risk.model.dao.PlayerDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.BorderModel;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 
 * @author i857625
 *
 */
public class RandomStrategy extends AbstractStrategy {

	/**
	 * 
	 * @param playerModel
	 */
	public RandomStrategy(PlayerModel playerModel) {
		super(playerModel);
	}

	@Override
	public void defend(String numDice) {
		super.defend(numDice);
	}

	@Override
	public void attack(CountryModel countryModelFrom, CountryModel countryModelTo, String numDice) {

		// get list of countries assigned to that player
		PlayerDaoImpl playerDaoImpl = new PlayerDaoImpl();
		List<CountryModel> countryModels =playerDaoImpl.getCountries(RunningGame.getInstance(),
				RunningGame.getInstance().getCurrentPlayer().getPlayerModel());

		// get a random country
		Random random = new Random();
		int i = random.nextInt(countryModels.size()-1);

		CountryModel attackFrom = countryModels.get(i);
		CountryModel attackTo = getRandomEnemyNeighbourCountry(attackFrom);

		// random number of times to attack a random country

		int noOfAttack = random.nextInt(10)+1;

		while(noOfAttack>0){
			System.out.println("number of attack" +  noOfAttack);

			if(attackFrom.getNumberOfArmies()>3) {
				numDice = "3";
			} else {
				numDice = "2";
			}

			String defenderNumDice;
			if(attackTo.getNumberOfArmies()>3 && "3".equalsIgnoreCase(numDice)) {
				defenderNumDice = "2";
			} else {
				defenderNumDice = "1";
			}

			super.attack(attackFrom, attackTo, numDice);
			super.defend(defenderNumDice);
			noOfAttack--;

		}


		fortify(null,null,0);

	}

	@Override
	public void fortify(CountryModel countryModelFrom, CountryModel countryModelTo, int numberOfArmies) {

		RunningGame.getInstance().getCurrentPlayer().getPlayerModel().setPlayingPhase("Fortify");
		RunningGame.getInstance().getSubject().markAndNotify();

		PlayerDaoImpl playerDaoImpl = new PlayerDaoImpl();
		List<CountryModel> countries = playerDaoImpl.getCountries(RunningGame.getInstance(),
				RunningGame.getInstance().getCurrentPlayer().getPlayerModel());

		Random random = new Random();

		int attempt=100;

		while (countryModelTo == null && attempt>0) {
			int index = random.nextInt(countries.size());
			countryModelFrom=countries.get(index);
			countryModelTo = getRandomNeighbourCountry(countryModelFrom);
			attempt--;
		}

		if (countryModelFrom!=null && countryModelTo!= null) {
			super.fortify(countryModelFrom, countryModelTo, numberOfArmies);
		} else {
			RunningGame.getInstance().moveToNextPlayer();
			RunningGame.getInstance().reinforceInitialization();
			RunningGame.getInstance().getSubject().markAndNotify();
			throw new RiskGameRuntimeException("There is no neighbour country to fortify");
		}
	}

	@Override
	public void reinforce(CountryModel countryModel, int numberOfArmies) {

		// get list of countries assigned to that player
		PlayerDaoImpl playerDaoImpl = new PlayerDaoImpl();
		List<CountryModel> countryModels =playerDaoImpl.getCountries(RunningGame.getInstance(),
				RunningGame.getInstance().getCurrentPlayer().getPlayerModel());

		// get a random country
		Random random = new Random();
		int i = random.nextInt(countryModels.size());

		CountryModel attackFrom = countryModels.get(i);

		super.reinforce(attackFrom, RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getReinforcementNoOfArmies());

	}

	private CountryModel getRandomNeighbourCountry(CountryModel fortifyFrom){

		// check at least one of neighbours countries is not an enemy
		BorderDaoImp borderDaoImpl = new BorderDaoImp();
		BorderModel borderModel = borderDaoImpl.findByName(RunningGame.getInstance(), fortifyFrom.getName());

		List<Integer> allNeighbours = borderModel.getNeighbours();

		CountryDaoImpl countryDao = new CountryDaoImpl();

		CountryModel fortifyTo = null;

		List<Integer> alliedNeighbours = new ArrayList<>();

		for(int i =0; i<allNeighbours.size();i++){
			// check if not an enemy
			//System.out.println("hi"+neighbours.get(i));
			if(allNeighbours.get(i) == RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getId()){
				alliedNeighbours.add(allNeighbours.get(i));
			}
		}

		// get random neighbours

		Random random = new Random();
		if(alliedNeighbours.size()>0) {
			int index = random.nextInt(alliedNeighbours.size() - 1);
			fortifyTo = countryDao.findById(RunningGame.getInstance(),alliedNeighbours.get(index));
		}

		return fortifyTo;


	}

	private CountryModel getRandomEnemyNeighbourCountry(CountryModel attackFrom) {

		// check at least one of neighbours countries is an enemy
		BorderDaoImp borderDaoImpl = new BorderDaoImp();
		BorderModel borderModel = borderDaoImpl.findByName(RunningGame.getInstance(), attackFrom.getName());

		List<Integer> allNeighbours = borderModel.getNeighbours();

		CountryDaoImpl countryDao = new CountryDaoImpl();

		CountryModel attackTo = null;

		List<Integer> enemyNeighbours = new ArrayList<>();

		for(int i =0; i<allNeighbours.size();i++){
			// check if not an enemy
			//System.out.println("hi"+neighbours.get(i));
			if(allNeighbours.get(i) != RunningGame.getInstance().getCurrentPlayer().getPlayerModel().getId()){
				enemyNeighbours.add(allNeighbours.get(i));
			}
		}

		// get random neighbours

		Random random = new Random();
		if(enemyNeighbours.size()>0) {
			int index = random.nextInt(enemyNeighbours.size());
			attackTo = countryDao.findById(RunningGame.getInstance(), enemyNeighbours.get(index));
		}

		return attackTo;

	}
}
