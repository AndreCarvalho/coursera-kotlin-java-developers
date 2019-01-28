package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
        this.allDrivers.minus(this.trips.distinctBy { it.driver }.map { it.driver })

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
        this.allPassengers.map { p -> Pair (p, this.trips.count { it.passengers.contains(p) }) }
                .filter { x -> x.second >= minTrips}
                .map { it.first }
                .toSet()

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
        this.allPassengers.filter { p -> this.trips.count { t -> t.driver == driver && t.passengers.contains(p) } > 1 }
                .toSet()

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
        this.allPassengers.asSequence()
                .map { p -> Pair(p, this.trips.filter { it.passengers.contains(p) }) }
                .map { (p, trips) ->
                    var partitions = trips.partition { t -> t.discount == null }
                    Triple(p, partitions.first, partitions.second)
                }
                .filter { (_, tripsFullPrice, tripsWithDiscount) -> tripsFullPrice.size < tripsWithDiscount.size }
                .map { it.first }
                .toSet()

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    val ranges = generateSequence(0..9) { x -> x.last+x.step..x.last+(x.last-x.start+x.step) }
    return this.trips.groupBy { t -> ranges.first { r -> r.contains(t.duration) } }
                     .maxBy { x -> x.value.size }?.key
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    val totalIncome = this.trips.sumByDouble { it.cost }
    val topDriversToCheck = Math.floor(this.allDrivers.size * 0.2).toInt()

    val topDriversAmount =
            this.trips.asSequence()
                    .groupBy { it.driver }
                    .map { entry -> entry.value.sumByDouble { it.cost } }
                    .sortedByDescending { it }
                    .take(topDriversToCheck)
                    .sum()

    return topDriversAmount > 0 && topDriversAmount >= totalIncome * .8
}