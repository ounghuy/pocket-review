package com.wadpam.rnr.web;

import com.wadpam.docrest.domain.RestCode;
import com.wadpam.docrest.domain.RestReturn;
import com.wadpam.rnr.domain.DLike;
import com.wadpam.rnr.json.JLike;
import com.wadpam.rnr.json.JProductV15;
import com.wadpam.rnr.json.JRating;
import com.wadpam.rnr.service.RnrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: mattias
 * Date: 7/24/12
 * Time: 7:20 PM
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping(value="{domain}/like")
public class LikeController {

    static final Logger LOG = LoggerFactory.getLogger(LikeController.class);

    private RnrService rnrService;

    /**
     * Add a like to a product.
     * @param productId domain-unique id for the product to like
     * @param username optional.
     * If authenticated, and RnrService.fallbackPrincipalName,
     * principal.name will be used if username is null.
     * @param latitude optional, -90..90
     * @param longitude optional, -180..180
     * @return the newly create like
     */
    @RestReturn(value=JLike.class, entity=JLike.class, code={
            @RestCode(code=200, message="OK", description="Like created")
    })
    @RequestMapping(value="", method= RequestMethod.POST)
    public RedirectView addLike(HttpServletRequest request,
                                             Principal principal,
                                             @RequestParam(required=true) String productId,
                                             @RequestParam(required=false) String username,
                                             @RequestParam(required=false) Float latitude,
                                             @RequestParam(required=false) Float longitude) {

        final DLike body = rnrService.addLike(productId, username,
                null != principal ? principal.getName() : null, latitude, longitude);

        return new RedirectView(request.getRequestURI() + "/" + body.getId().toString());
    }

    /**
     * Delete a like with a specific id.
     * @param id The unique like id
     * @return the and http response code indicating the outcome of the operation
     */
    @RestReturn(value=JLike.class, entity=JLike.class, code={
            @RestCode(code=200, message="OK", description="Like deleted"),
            @RestCode(code=404, message="NOK", description="Like not found and can not be deleted")
    })
    @RequestMapping(value="{id}", method= RequestMethod.DELETE)
    public ResponseEntity<JLike> deleteLike(HttpServletRequest request,
                                                      Principal principal,
                                                      @PathVariable long id) {

        final DLike body = rnrService.deleteLike(id);

        if (null == body)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<JLike>(HttpStatus.OK);
    }

    /**
     * Get like details for a specific id.
     * @param id The unique like id
     * @return the like details
     */
    @RestReturn(value=JLike.class, entity=JLike.class, code={
            @RestCode(code=200, message="OK", description="Like found"),
            @RestCode(code=404, message="NOK", description="Like not found")
    })
    @RequestMapping(value="{id}", method= RequestMethod.GET)
    public ResponseEntity<JLike> getLike(HttpServletRequest request,
                                            Principal principal,
                                            @PathVariable long id) {

        final DLike body = rnrService.getLike(id);

        if (null == body)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<JLike>(Converter.convert(body, request), HttpStatus.OK);
    }

    /**
     * Returns all likes done by a specific user.
     * @param username optional.
     * If authenticated, and RnrService.fallbackPrincipalName,
     * principal.name will be used if username is null.
     * @return a list of my likes
     */
    @RestReturn(value=JLike.class, entity=JLike.class, code={
            @RestCode(code=200, message="OK", description="Likes found for user")
    })
    @RequestMapping(value="my", method= RequestMethod.POST)
    public ResponseEntity<Collection<JLike>> getMyLikes(HttpServletRequest request,
                                                        Principal principal,
                                                        @RequestParam(required=false) String username) {

        try {
            final Collection<DLike> body = rnrService.getMyLikes(username,
                    null != principal ? principal.getName() : null);

            return new ResponseEntity<Collection<JLike>>((Collection<JLike>)Converter.convert(body, request),
                    HttpStatus.OK);
        }
        catch (IllegalArgumentException usernameNull) {
            return new ResponseEntity<Collection<JLike>>(HttpStatus.UNAUTHORIZED);
        }
    }


    // Setters and Getters
    public void setRnrService(RnrService rnrService) {
        this.rnrService = rnrService;
    }
}
