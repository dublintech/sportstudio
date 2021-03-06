package com.donabate.staveley.alex.api.endpoints;

import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.donabate.staveley.alex.api.validation.TeamApiValidator;
import com.donabate.staveley.alex.pojos.resource.LinkHolder;
import com.donabate.staveley.alex.pojos.resource.ResourceListWrapper;
import com.donabate.staveley.alex.pojos.team.CreateTeamCommand;
import com.donabate.staveley.alex.pojos.team.Team;
import com.donabate.staveley.alex.pojos.team.TeamQuery;
import com.donabate.staveley.alex.pojos.team.jersey.CreateJerseyCommand;
import com.donabate.staveley.alex.pojos.team.jersey.Jersey;
import com.donabate.staveley.alex.service.TeamService;

@Service
@Path("/teams/{teamId}/jerseys")
public class JerseyApi {
	
    private static final Logger LOG = LoggerFactory.getLogger(JerseyApi.class);
    
    // ToDo consider if it s better the API invoked this or if the teamService does. 
    @Autowired
    private TeamService teamService;
	
    /**
     * On command line do:
     * <pre>
     * > curl localhost:8080/teams/123/jerseys/22
     * </pre>
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{jerseyId}")
    public Response getSingle(@PathParam("teamId") String teamId, 
    		@PathParam("jerseyId") String jerseyId) {
    	LOG.info(">>getSingle(), teamId=" + teamId + ",jerseyId=" + jerseyId);
    	
    	Jersey jersey = teamService.findJersey(teamId, jerseyId);
  
    	GenericEntity<Jersey> myEntity = 
    			new GenericEntity<Jersey>(jersey) {};
    	return Response.status(200).entity(myEntity).build();
    }
    
    
    /**
     * To test do:
     * <pre>
     * curl -X POST -v -d "{"""type""":"""h""", """colour""": """red"""}" 
     * 			localhost:8080/teams/123/jerseys --header "Content-Type:application/json"
     * </pre>
     * @param createTeamCommand
     * @return
     */   
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createJersey(@PathParam("teamId") String teamId, @Valid CreateJerseyCommand createJerseyCommand) {
        LOG.info(">>createJersey(createJerseyCommand=" + createJerseyCommand + ")");
    	Jersey jersey = teamService.createJersey(teamId, createJerseyCommand);
    	GenericEntity<Jersey> myTeam = 
    			new GenericEntity<Jersey>(jersey) {};
    	return Response.status(201).header("location", jersey.getLinks().get(LinkHolder.SELF)).entity(myTeam).build();
    }
    

}
