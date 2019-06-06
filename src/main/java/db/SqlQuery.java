package db;

/**
 * Created by haimin-a on 06.06.2019.
 */
public class SqlQuery {
    public static String CDI_PHONE = "select ca_id from contract_party c\n" +
            "where c.icc_id = ?\n" +
            "and c.version = 0\n" +
            "and c.ca_id is not null\n" +
            "and c.enddate is null";

    public static String CDI_MODEM = "select ca_id from agreement a\n" +
            "where a.enddate is null\n" +
            "and a.version = 0\n" +
            "and a.ca_id is not null\n" +
            "and a.iccid = ?";

    public static String CA = "select c.iccid, texts.document_text agreement, doc.cms eds, doc.signed_data sign, doc.document_id id\n" +
            "from CA.contracts c\n" +
            "join CA.certificates cer on cer.contract_id = c.contract_id\n" +
            "join CA.documents doc on doc.certificate_id = cer.certificate_id\n" +
            "join CA.document_texts texts on texts.document_text_id = doc.document_text_id\n" +
            "where c.iccid = ?";
}
