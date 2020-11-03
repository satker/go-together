import React from 'react';
import PropTypes from "prop-types";
import {Card, CardActionArea, CardActions, CardContent, CardMedia, Typography} from "@material-ui/core";
import {getCorrectDateFromString, getSrcForImg} from "forms/utils/utils";
import {Event} from "forms/utils/types";
import DeleteIcon from "forms/utils/components/Icon/Delete";
import {PHOTO_OBJECT} from "forms/utils/constants";
import EventLikes from "forms/utils/components/Event/EventLikes";
import {connect} from "App/Context";

const ItemEvent = ({event, onDelete, userId, eventIds}) => <Card>
        {event.author.id === userId && <DeleteIcon onDelete={() => onDelete(event.id)}/>}
    <CardActionArea href={'/events/' + event.id}>
        <CardMedia
            component="img"
            height="250"
            image={getSrcForImg(event.groupPhoto.photos[0] || {...PHOTO_OBJECT})}
        />
        <CardContent>
            <Typography>{event.name}</Typography>
            <Typography>{event.description}</Typography>
            <Typography>I {event.author.firstName}, {event.author.lastName}</Typography>
            <Typography>From {event.author.location.locations[0].place.name},
                {event.author.location.locations[0].place.country.name}</Typography>
            <Typography>Languages: {event.author.languages.map(lang => lang.name).join(', ')}</Typography>
            <Typography>My
                interests: {event.author.interests.map(interest => interest.name).join(', ')}</Typography>
            <Typography>Dates: {getCorrectDateFromString(event.startDate)} -> {getCorrectDateFromString(event.endDate)}</Typography>
            <Typography>Going to travel through {event.route.locations.map(location => location.place.name + ", " +
                location.place.country.name).join(" -> ")}</Typography>
            <Typography>With {event.peopleCount} friends</Typography>
        </CardContent>
    </CardActionArea>
        <CardActions>
            {userId && userId !== event.author.id &&
            <EventLikes eventId={event.id} eventIds={eventIds}/>}
        </CardActions>
    </Card>;

ItemEvent.propTypes = {
    event: Event.isRequired,
    onDelete: PropTypes.func,
    userId: PropTypes.string,
    eventIds: PropTypes.arrayOf(PropTypes.string).isRequired
};

const mapStateToProps = (state) => ({
    userId: state.auth.response.userId
});

export default connect(mapStateToProps, null)(ItemEvent);