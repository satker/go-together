import React from "react";
import MultipleMap from "forms/utils/components/ObjectGeoLocation/MultipleMap";
import EventsList from "./EventsList";

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

export default Map;