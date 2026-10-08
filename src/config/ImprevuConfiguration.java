package config;

/**
 * Constantes pour les imprevus.
 */
public class ImprevuConfiguration {

    public static final int IMPREVU_COUNT = 5;

    public static final int OVERSLEEP = 1;
    public static final int SURPRISE_VISIT = 2;
    public static final int POWER_OUTAGE = 3;
    public static final int SICK_DAY = 4;
    public static final int RUSH_DAY = 5;

    public static final int PROBABILITY_DENOMINATOR = 5;
    public static final int TRIGGER_VALUE = 1;

    public static final int OVERSLEEP_DELAY_MIN = 60;
    public static final int SURPRISE_VISIT_SLEEP_HOUR = 23;
    public static final int SURPRISE_VISIT_SLEEP_MIN = 59;
    public static final int RUSH_DAY_DURATION_FACTOR_PERCENT = 50;
    public static final int RUSH_DAY_WALK_BUFFER_MIN = 5;
}
