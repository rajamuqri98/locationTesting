package com.test.dummylocation.sync;

public class SyncKey {

    public static final String TRANSACTIONS_ARR = "\"transactions\": [";
    public static final String TENDERS_ARR = "\"tenders\": [";
    public static final String COMBINEFROM_ARR = "\"combine_from_bill\": [";
    public static final String DETAILS_ARR = "\"transaction_details\": [";
    public static final String COMBOS_ARR = "\"combos\": [";
    public static final String CONDIMENTS_ARR = "\"condiments\": [";
    public static final String VOIDS_ARR = "\"void_reasons\": [";

    // -- 1
    public static final String INVOICEID_KEY = "\"invoice_id\": ";
    public static final String BILLREFID_KEY = "\"bill_ref_id\": ";
    public static final String TABLEVODE_KEY = "\"table_code\": ";
    public static final String TOTALAMOUNT_KEY = "\"total_amount\": ";
    public static final String AMOUNTPAID_KEY = "\"amount_paid\": ";
    public static final String AMOUNTBAL_KEY = "\"amount_balance\": ";
    public static final String ROUNDAMT_KEY = "\"round_amount\": ";
    public static final String GROSSAMT_KEY = "\"gross_amount\": ";
    public static final String TAXTOTAL_KEY = "\"tax_total\": ";
    public static final String SERVICECHARGETOTAL_KEY = "\"service_charge_total\": ";
    public static final String DISCTOTAL_KEY = "\"discount_total\": ";
    public static final String BILLDISC_KEY = "\"bill_discount\": ";
    public static final String BILLDISCREF_KEY = "\"bill_discount_ref\": ";
    public static final String BILLDISCAUTHBY_KEY = "\"bill_discount_auth_by\": ";
    public static final String BILLDISCDESCS_KEY = "\"bill_discount_description\": ";
    public static final String BILLVOIDSTATUS_KEY = "\"bill_void_status\": ";
    public static final String BILLVOIDREF_KEY = "\"bill_void_ref\": ";
    public static final String BILLVOIDAUTH_KEY = "\"bill_void_auth\": ";
    public static final String OPENBY_KEY = "\"open_by\": ";
    public static final String CLOSEBY_KEY = "\"close_by\": ";
    public static final String OPENBILL_KEY = "\"open_bill\": ";
    public static final String CLOSEBILL_KEY = "\"close_bill\": ";
    public static final String PAYDISC_KEY = "\"payment_discount\": ";
    public static final String PRINTCOUNT_KEY = "\"print_count\": ";

    // -- 2
    public static final String ID_TENDERKEY = "\"tender_id\": ";
    public static final String AMT_TENDERKEY = "\"amount\": ";
    public static final String CHANGE_TENDERKEY = "\"change\": ";
    public static final String STATUS_TENDERKEY = "\"transaction_status\": ";
    public static final String REMARK_TENDERKEY = "\"remark\": ";

    // -- 3
    public static final String MENUID_DETAILKEY = "\"menu_item_id\": ";
    public static final String MENUNAME_DETAILKEY = "\"menu_name\": ";
    public static final String SALESTYPE_DETAILKEY = "\"sales_type_id\": ";
    public static final String UNITPRICE_DETAILKEY = "\"unit_price\": ";
    public static final String PRICE_DETAILKEY = "\"price\": ";
    public static final String COST_DETAILKEY = "\"cost\": "; //TODO add on release 1.7.3
    public static final String GROSSPRICE_DETAILKEY = "\"gross_price\": ";
    public static final String AMOUNT_DETAILKEY = "\"amount\": ";
    public static final String GROSSAMT_DETAILKEY = "\"gross_amount\": ";
    public static final String TAX_DETAILKEY = "\"tax\": ";
    public static final String TAXRATE_DETAILKEY = "\"tax_rate\": ";
    public static final String SERVICECHARGERATE_DETAILKEY = "\"service_charge_rate\": ";
    public static final String SERVICECHARGE_DETAILKEY = "\"service_charges\": ";
    public static final String TAXTYPE_DETAILKEY = "\"tax_type_id\": ";
    public static final String DISC_DETAILKEY = "\"discount\": ";
    public static final String DISCREF_DETAILKEY = "\"discount_ref\": ";
    public static final String DISCAUTHBY_DETAILKEY = "\"discount_auth_by\": ";
    public static final String DISCDESCS_DETAILKEY = "\"discount_description\":";
    public static final String DISCRATE_DETAILKEY = "\"discount_rate\": ";
    public static final String QTY_DETAILKEY = "\"quantity\": ";
    public static final String TRANSACTIONSTATUS_DETAILKEY = "\"transaction_status\": ";
    public static final String HASCONDIMENT_DETAILKEY = "\"has_condiment\": ";
    public static final String ORDERTIME_DETAILKEY = "\"order_time\": ";
    public static final String REFID_DETAILKEY = "\"bill_detail_ref_id\": ";
    public static final String ORDERBY_DETAILKEY = "\"order_by\": ";
    public static final String HASVOID_DETAILKEY = "\"is_has_void\": ";

    // -- 4
    public static final String MENUID_COMBOKEY = "\"menu_id\": ";
    public static final String DESCS_COMBOKEY = "\"descs\": ";
    public static final String DESCS_COMBOKEY2 = "\"descs2\": ";
    public static final String QTY_COMBOKEY = "\"qty\": ";
    public static final String EXTRAPRICE_COMBOKEY = "\"extra_price\": ";
    public static final String HASCONDI_COMBOKEY = "\"is_has_condi\": ";

    // -- 5
    public static final String ID_CONDIKEY = "\"id\": ";
    public static final String DESCS_CONDIKEY = "\"descs\": ";
    public static final String PRICE_CONDIKEY = "\"price\": ";
    public static final String DISC_CONDIKEY = "\"disc\": ";
    public static final String DISCRATE_CONDIKEY = "\"disc_rate\": ";
    public static final String TAX_CONDIKEY = "\"tax\": ";
    public static final String SERVICECGARGE_CONDIKEY = "\"service_charge\": ";
    public static final String AMT_CONDIKEY = "\"amount\": ";
    public static final String GROSSPRICE_CONDIKEY = "\"gross_price\": ";

    // -- 6
    public static final String REASON_VOIDKEY = "\"reason\": ";
    public static final String REMARKS_VOIDKEY = "\"extra_remarks\": ";
    public static final String QTY_VOIDKEY = "\"qty\": ";
    public static final String UNITPRICE_VOIDKEY = "\"void_unit_price\": ";
    public static final String AMT_VOIDKEY = "\"void_amount\": ";
    public static final String REF_VOIDKEY = "\"void_ref\": ";
    public static final String AUTHBY_VOIDKEY = "\"void_authorized_by\": ";

    //-- 7
    public static final String KEY_DATA = "\"data\": ";
    public static final String KEY_OPENBY = "\"open_by\": ";
    public static final String KEY_OPENDATE = "\"open_date\": ";

    public static final String KEY_BDAY_ID = "\"business_day_id\": ";
    public static final String KEY_CLOSEBY = "\"close_by\": ";
    public static final String KEY_CLOSEDATE = "\"close_date\": ";
    public static final String KEY_BEND_STARTREF = "\"start_bill_ref_id\": ";
    public static final String KEY_BEND_ENDREF = "\"end_bill_ref_id\": ";

    public static final String KEY_STARTBY = "\"start_by\": ";
    public static final String KEY_STARTSHIFT = "\"start_shift\": ";
    public static final String KEY_FLOATAMT = "\"float_amount\": ";

    public static final String KEY_BSHIFT_ID = "\"business_shift_id\": ";
    public static final String KEY_ENDBY = "\"end_by\": ";
    public static final String KEY_ENDSHIFT = "\"end_shift\": ";
    public static final String KEY_ENDSHIFT_STARTREF = "\"start_bill_ref_id\": ";
    public static final String KEY_ENDSHIFT_ENDREF = "\"end_bill_ref_id\": ";

    public static final String VOIDP_ARR_TRX = "\"transactions\": [";
    public static final String VOIDP_KEY_INVOICEID = "\"invoice_id\": ";
    public static final String VOIDP_KEY_VOIDSTS = "\"bill_void_status\": ";
    public static final String VOIDP_KEY_VOIDREF = "\"bill_void_ref\": ";
    public static final String VOIDP_KEY_VOIDAUTH = "\"bill_void_auth\": ";
    public static final String VOIDP_ARR_REASONS = "\"void_reasons\": [";
    public static final String VOIDP_KEY_IREASON = "\"reason\": ";
    public static final String VOIDP_KEY_IQTY = "\"qty\": ";
    public static final String VOIDP_KEY_IUPRICE = "\"void_unit_price\": ";
    public static final String VOIDP_KEY_IAMT = "\"void_amount\": ";
    public static final String VOIDP_KEY_IVOIDREF = "\"void_ref\": ";
    public static final String VOIDP_KEY_IVOIDAUTHBY = "\"void_authorized_by\": ";

    //-- LocationKey
    public static final String KEY_LOC = "\"location\": {";
    public static final String KEY_LAT = "\"latitude\": ";
    public static final String KEY_LNG = "\"longitude\": ";
    public static final String KEY_DTE = "\"date\": ";

}
