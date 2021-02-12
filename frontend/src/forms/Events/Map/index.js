import React from "react";
import PropTypes from 'prop-types';
import MultipleMap from "forms/utils/components/ObjectGeoLocation/MultipleMap";
import EventsList from "./EventsList";
import {Event} from 'forms/utils/types'

const Map = ({events}) => {
    const eventLocations = events.map(event => ({
        id: event.id,
        name: event.name,
        locations: event.routeInfo.infoRoutes.map(route => ({routeNumber: route.routeNumber, location: route.location}))
    }));
    return <MultipleMap routes={eventLocations}>
        <EventsList/>
    </MultipleMap>
};

Map.propTypes = {
    events: PropTypes.arrayOf(Event)
}

export default Map;