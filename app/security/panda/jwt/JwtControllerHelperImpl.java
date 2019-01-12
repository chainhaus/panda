package security.panda.jwt;

import play.Logger;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.Optional;
import java.util.function.Function;

public class JwtControllerHelperImpl implements JwtControllerHelper{

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private JwtValidator jwtValidator;

    @Inject
    public JwtControllerHelperImpl(JwtValidator jwtValidator) {
        this.jwtValidator = jwtValidator;
    }

    @Override
    public Result verify(Http.Request request, Function<F.Either<JwtValidator.Error, VerifiedJwt>, Result> f) {
        Optional<String> authHeader =  request.getHeaders().get(HEADER_AUTHORIZATION);

        if (!authHeader.filter(ah -> ah.contains(BEARER)).isPresent()) {
            Logger.error("f=JwtControllerHelperImpl, event=verify, error=authHeaderNotPresent");
        }

        String token = authHeader.map(ah -> ah.replace(BEARER, "")).orElse("");
        return f.apply(jwtValidator.verify(token));
    }
}
