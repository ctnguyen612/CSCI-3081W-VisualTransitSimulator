package edu.umn.cs.csci3081w.project.model;

public class TrainFactory implements VehicleFactory {
  private GenerationStrategy generationStrategy;
  private Counter counter;
  private StorageFacility storageFacility;

  /**
   * Initializes the strategy depending on time of day when simulation is started.
   *
   * @param storageFacility   storage facility information
   * @param counter           counter information
   * @param time              hour of the day
   */
  public TrainFactory(StorageFacility storageFacility, Counter counter, int time) {
    if (time >= 8 && time < 16) {
      generationStrategy = new TrainStrategyDay();
    } else {
      generationStrategy = new TrainStrategyNight();
    }
    this.storageFacility = storageFacility;
    this.counter = counter;
  }

  @Override
  public Vehicle generateVehicle(Line line) {
    String typeOfVehicle = generationStrategy.getTypeOfVehicle(storageFacility);
    Vehicle generatedVehicle = null;

    if (typeOfVehicle != null && typeOfVehicle.equals(ElectricTrain.ELECTRIC_TRAIN_VEHICLE)) {
      generatedVehicle = new GreenVehicleDecorator(
        new ElectricTrain(counter.getElectricTrainIdCounterAndIncrement(),
          line, ElectricTrain.CAPACITY,
          ElectricTrain.SPEED)
      );
      storageFacility.decrementElectricTrainsNum();
    } else if (typeOfVehicle != null && typeOfVehicle.equals(DieselTrain.DIESEL_TRAIN_VEHICLE)) {
      generatedVehicle = new YellowVehicleDecorator(
        new DieselTrain(counter.getDieselTrainIdCounterAndIncrement(),
          line, DieselTrain.CAPACITY,
          DieselTrain.SPEED)
      );
      storageFacility.decrementDieselTrainsNum();
    }

    return generatedVehicle;
  }

  @Override
  public void returnVehicle(Vehicle vehicle) {
    if (vehicle == null) {
      return;
    }
    if (vehicle.getBaseVehicle() instanceof ElectricTrain) {
      storageFacility.incrementElectricTrainsNum();
    } else if (vehicle.getBaseVehicle() instanceof DieselTrain) {
      storageFacility.incrementDieselTrainsNum();
    }
  }

  /**
   * Get storage facility.
   *
   * @return storage facility
   */
  public StorageFacility getStorageFacility() {
    return storageFacility;
  }

  /**
   * Get generation strategy.
   *
   * @return generation strategy
   */
  public GenerationStrategy getGenerationStrategy() {
    return generationStrategy;
  }

}
