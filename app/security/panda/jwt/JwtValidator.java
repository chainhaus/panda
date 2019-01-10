package security.panda.jwt;

import play.libs.F;

public interface JwtValidator {

    enum Error {
        ERR_INVALID_SIGNATURE_OR_CLAIM,
        TOKEN_EXPIRED
    }

    F.Either<Error, VerifiedJwt> verify(String token);

}
