import React from "react";
import {connect} from "App/Context";
import MultipleMap from "forms/utils/components/ObjectGeoLocation/MultipleMap";

const Map = ({events}) => {
    const eventLocations = events.map(event => ({
        id: event.id,
        name: event.name,
        locations: event.routeInfo.infoRoutes.map(route => ({routeNumber: route.routeNumber, location: route.location}))
    }));
    return <MultipleMap routes={eventLocations}/>
};

const mapStateToProps = state => ({});


export default connect(mapStateToProps)(Map);