package filters.jwt;

import play.libs.typedmap.TypedKey;
import security.panda.jwt.VerifiedJwt;

public class Attrs {
    public static final TypedKey<VerifiedJwt> VERIFIED_JWT = TypedKey.<VerifiedJwt>create("verifiedJwt");
}
