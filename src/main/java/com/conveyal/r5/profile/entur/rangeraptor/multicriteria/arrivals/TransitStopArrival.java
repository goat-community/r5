package com.conveyal.r5.profile.entur.rangeraptor.multicriteria.arrivals;


import com.conveyal.r5.profile.entur.api.TripScheduleInfo;

public final class TransitStopArrival<T extends TripScheduleInfo> extends AbstractStopArrival<T> {
    private final T trip;
    private boolean arrivedByTransitLastRound = true;

    public TransitStopArrival(AbstractStopArrival<T> previousState, int round, int stopIndex, int arrivalTime, int boardTime, T trip) {
        super(
                timeShifted(previousState, boardTime),
                round,
                round * 2,
                stopIndex,
                boardTime,
                arrivalTime,
                previousState.cost()
        );
        this.trip = trip;
    }

    @Override
    public boolean arrivedByTransit() {
        return true;
    }

    @Override
    public T trip() {
        return trip;
    }

    @Override
    public int boardStop() {
        return previousStop();
    }

    /**
     * This method return true if we arrived at this stop in the last round.
     * <p/>
     * NOTE! This method does not know about witch round it is, it just assume
     * that is will be called ONCE pr round and that it can return 'true' the
     * first time it is called.
     */
    public boolean arrivedByTransitLastRound() {
        if (arrivedByTransitLastRound) {
            arrivedByTransitLastRound = false;
            return true;
        }
        return false;
    }

    private static <T extends TripScheduleInfo> AbstractStopArrival<T> timeShifted(
            AbstractStopArrival<T> previous, int boardTime
    ) {
        return previous.arrivedByAccessLeg()
                ? ((AccessStopArrival<T>) previous).timeShifted(boardTime)
                : previous;
    }
}
