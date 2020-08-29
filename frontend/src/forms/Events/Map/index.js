import React from "react";
import {connect} from "App/Context";
import MultipleMap from "../../utils/components/ObjectGeoLocation/MultipleMap";

const Map = ({events}) => {
    const eventLocations = events.map(event => ({
        id: event.id,
        name: event.name,
        locations: event.route.locations
    }));
    return <MultipleMap editable={false}
                        routes={eventLocations}/>
};

const mapStateToProps = state => ({});


export default connect(mapStateToProps)(Map);