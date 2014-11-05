package jp.evo.btm.common;

public class ConstantValues {
	public static String COMPANY_NAME = "t-btm";
	public static final int MALE = 1;
	public static final int FEMALE = 2;

	public static final int ROLE_ADMIN = 2;
	public static final int ROLE_NORMAL = 1;

	public static final int TRAVEL_STATION_STATUS_PENDING_APPROVE = 1;
	public static final int TRAVEL_STATION_STATUS_PENDING_APPLY = 0;
	public static final int TRAVEL_STATION_STATUS_INIT = -2;
	public static final int TRAVEL_STATION_STATUS_PENDING_TICKET = -1;
	public static final int TRAVEL_STATION_STATUS_CANCEL = 2;
	public static final int TRAVEL_STATION_STATUS_OK = 8;

	public static final int INT_VALUE_TRUE = 1;
	public static final int INT_VALUE_FALSE = 0;

	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATE_SPLITER = "-";
	public static final long SECOND_OF_A_DAY = 24 * 3600 *1000;
	public static final int RANGE_TO_DISPLAY_PACKAGE = 10;
	
	public static final int TICKET_TYPE_PACKAGE = 1;
	public static final int TICKET_TYPE_PLANE = 2;
	public static final int TICKET_TYPE_SHINKANSEN = 3;
}
