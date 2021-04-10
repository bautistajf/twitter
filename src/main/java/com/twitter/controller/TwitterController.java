package com.twitter.controller;

import static com.twitter.utils.ControllerHelper.PATH;
import static org.springframework.http.HttpStatus.OK;

import com.twitter.dto.ErrorDTO;
import com.twitter.dto.TweetDTO;
import com.twitter.entity.TweetEntity;
import com.twitter.exception.ServiceException;
import com.twitter.facade.TwitterFacade;
import com.twitter.mapper.TwitterMapper;
import com.twitter.utils.ControllerHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = PATH, tags = "tweets")
@Log4j2
@RequiredArgsConstructor
public class TwitterController {

    private final TwitterMapper mapper;

    private final TwitterFacade facade;

    @ApiOperation(
        value = "Obtiene los tweets",
        notes = "Retorna los tweets",
        response = TweetDTO.class,
        responseHeaders = {
            @ResponseHeader(name = ControllerHelper.X_PROCESSTIME,
                description = ControllerHelper.X_PROCESSTIME_DESC, response = Integer.class),
            @ResponseHeader(name = ControllerHelper.X_INIT_TIMESTAMP,
                description = ControllerHelper.X_INIT_TIMESTAMP_DESC, response = LocalDateTime.class),
            @ResponseHeader(name = ControllerHelper.X_REQUESTHOST,
                description = ControllerHelper.X_REQUESTHOST_DESC, response = String.class),
            @ResponseHeader(name = ControllerHelper.X_SERVICENAME,
                description = ControllerHelper.X_SERVICENAME_DESC, response = String.class),
            @ResponseHeader(name = ControllerHelper.X_VERSION,
                description = ControllerHelper.X_VERSION_DESC, response = String.class)
        }
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDTO.class),
        @ApiResponse(code = 403, message = "Forbidden", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Failure", response = ErrorDTO.class)
    })
    @GetMapping
    public ResponseEntity<List<TweetDTO>> getAllTwitter() {
        LocalDateTime timestamp = LocalDateTime.now();
        HttpHeaders httpHeaders = ControllerHelper.getHeaders(timestamp);

        List<TweetDTO> twitters = mapper.toDTOList(facade.getTweets());

        httpHeaders.set(ControllerHelper.HEADER_TOTAL_COUNT, String.valueOf(twitters.size()));
        ControllerHelper.setProcessTime(timestamp, httpHeaders);
        return new ResponseEntity<>(twitters, httpHeaders, OK);
    }

    @ApiOperation(
        value = "Obtiene los tweets validados por usuario",
        notes = "Retorna los tweets validados por usuario",
        response = TweetDTO.class,
        responseHeaders = {
            @ResponseHeader(name = ControllerHelper.X_PROCESSTIME,
                description = ControllerHelper.X_PROCESSTIME_DESC, response = Integer.class),
            @ResponseHeader(name = ControllerHelper.X_INIT_TIMESTAMP,
                description = ControllerHelper.X_INIT_TIMESTAMP_DESC, response = LocalDateTime.class),
            @ResponseHeader(name = ControllerHelper.X_REQUESTHOST,
                description = ControllerHelper.X_REQUESTHOST_DESC, response = String.class),
            @ResponseHeader(name = ControllerHelper.X_SERVICENAME,
                description = ControllerHelper.X_SERVICENAME_DESC, response = String.class),
            @ResponseHeader(name = ControllerHelper.X_VERSION,
                description = ControllerHelper.X_VERSION_DESC, response = String.class)
        }
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDTO.class),
        @ApiResponse(code = 403, message = "Forbidden", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Failure", response = ErrorDTO.class)
    })
    @GetMapping(value = "/validadosPorUsuario/{userName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TweetDTO>> getAllTwitterValidatedByUser(
        @ApiParam(value = "Name of the user", example = "Javier") @PathVariable(value = "userName") final String userName
    ) {
        LocalDateTime timestamp = LocalDateTime.now();
        HttpHeaders httpHeaders = ControllerHelper.getHeaders(timestamp);

        List<TweetDTO> twitters = mapper.toDTOList(facade.getTweetsValidatedByUser(userName));

        httpHeaders.set(ControllerHelper.HEADER_TOTAL_COUNT, String.valueOf(twitters.size()));
        ControllerHelper.setProcessTime(timestamp, httpHeaders);
        return new ResponseEntity<>(twitters, httpHeaders, OK);
    }

    @ApiOperation(
        value = "Validar un tweet por id",
        notes = "Retorna el tweet validado por id",
        response = TweetDTO.class,
        responseHeaders = {
            @ResponseHeader(name = ControllerHelper.X_PROCESSTIME,
                description = ControllerHelper.X_PROCESSTIME_DESC, response = Integer.class),
            @ResponseHeader(name = ControllerHelper.X_INIT_TIMESTAMP,
                description = ControllerHelper.X_INIT_TIMESTAMP_DESC, response = LocalDateTime.class),
            @ResponseHeader(name = ControllerHelper.X_REQUESTHOST,
                description = ControllerHelper.X_REQUESTHOST_DESC, response = String.class),
            @ResponseHeader(name = ControllerHelper.X_SERVICENAME,
                description = ControllerHelper.X_SERVICENAME_DESC, response = String.class),
            @ResponseHeader(name = ControllerHelper.X_VERSION,
                description = ControllerHelper.X_VERSION_DESC, response = String.class)
        }
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDTO.class),
        @ApiResponse(code = 403, message = "Forbidden", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Failure", response = ErrorDTO.class)
    })
    @PutMapping(value = "/validar/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TweetDTO> validateTweet(
        @ApiParam(value = "Id of tweet", example = "1") @PathVariable(value = "id") final Long id
    ) throws ServiceException {
        LocalDateTime timestamp = LocalDateTime.now();
        HttpHeaders httpHeaders = ControllerHelper.getHeaders(timestamp);

        TweetDTO tweet = mapper.toDto(facade.validateTweet(id));

        ControllerHelper.setProcessTime(timestamp, httpHeaders);
        return new ResponseEntity<>(tweet, httpHeaders, OK);
    }

    @ApiOperation(
        value = "Obtiene los N hashtags más usados (default 10).",
        notes = "Retorna los N hashtags más usados (default 10).",
        response = TweetDTO.class,
        responseHeaders = {
            @ResponseHeader(name = ControllerHelper.X_PROCESSTIME,
                description = ControllerHelper.X_PROCESSTIME_DESC, response = Integer.class),
            @ResponseHeader(name = ControllerHelper.X_INIT_TIMESTAMP,
                description = ControllerHelper.X_INIT_TIMESTAMP_DESC, response = LocalDateTime.class),
            @ResponseHeader(name = ControllerHelper.X_REQUESTHOST,
                description = ControllerHelper.X_REQUESTHOST_DESC, response = String.class),
            @ResponseHeader(name = ControllerHelper.X_SERVICENAME,
                description = ControllerHelper.X_SERVICENAME_DESC, response = String.class),
            @ResponseHeader(name = ControllerHelper.X_VERSION,
                description = ControllerHelper.X_VERSION_DESC, response = String.class)
        }
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDTO.class),
        @ApiResponse(code = 403, message = "Forbidden", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Failure", response = ErrorDTO.class)
    })
    @GetMapping(value = "/hashTagMoreUsed")
    public ResponseEntity<List<String>> getHashTagMoreUsed() throws ServiceException {
        LocalDateTime timestamp = LocalDateTime.now();
        HttpHeaders httpHeaders = ControllerHelper.getHeaders(timestamp);

        List<String> hashTagMoreUsed = facade.getHashTagsMoreUsed();

        httpHeaders.set(ControllerHelper.HEADER_TOTAL_COUNT, String.valueOf(hashTagMoreUsed.size()));
        ControllerHelper.setProcessTime(timestamp, httpHeaders);
        return new ResponseEntity<>(hashTagMoreUsed, httpHeaders, OK);
    }

    @ApiOperation(
        value = "Obtiene los tweets validados agrupados por usuario",
        notes = "Retorna los tweets validados agrupados por usuario",
        response = TweetDTO.class,
        responseHeaders = {
            @ResponseHeader(name = ControllerHelper.X_PROCESSTIME,
                description = ControllerHelper.X_PROCESSTIME_DESC, response = Integer.class),
            @ResponseHeader(name = ControllerHelper.X_INIT_TIMESTAMP,
                description = ControllerHelper.X_INIT_TIMESTAMP_DESC, response = LocalDateTime.class),
            @ResponseHeader(name = ControllerHelper.X_REQUESTHOST,
                description = ControllerHelper.X_REQUESTHOST_DESC, response = String.class),
            @ResponseHeader(name = ControllerHelper.X_SERVICENAME,
                description = ControllerHelper.X_SERVICENAME_DESC, response = String.class),
            @ResponseHeader(name = ControllerHelper.X_VERSION,
                description = ControllerHelper.X_VERSION_DESC, response = String.class)
        }
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDTO.class),
        @ApiResponse(code = 403, message = "Forbidden", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Failure", response = ErrorDTO.class)
    })
    @GetMapping(value = "/allTweetsValidatedByUsers")
    public ResponseEntity<Map<String, List<TweetEntity>>>  getAllTweetsValidatedByUsers() {
        LocalDateTime timestamp = LocalDateTime.now();
        HttpHeaders httpHeaders = ControllerHelper.getHeaders(timestamp);

        Map<String, List<TweetEntity>>   tweetList = facade.getAllTweetsValidatedByUsers();

        httpHeaders.set(ControllerHelper.HEADER_TOTAL_COUNT, String.valueOf(tweetList.size()));
        ControllerHelper.setProcessTime(timestamp, httpHeaders);
        return new ResponseEntity<>(tweetList, httpHeaders, OK);
    }
}
