package security.panda.jwt;

import java.util.Date;

public interface VerifiedJwt {

    String getHeader();
    String getPayload();
    String getIssuer();
    Date getExpiresAt();

}
