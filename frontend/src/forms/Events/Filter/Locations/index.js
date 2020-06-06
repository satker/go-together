import React from "react";
import PropTypes from "prop-types";

import AutocompleteLocation from "forms/utils/components/AutocompleteLocation";
import {FilterOperator} from "forms/utils/utils";
import {SearchObject} from "forms/utils/types";
import ItemContainer from "forms/utils/components/Container/ItemContainer";
import Container from "forms/utils/components/Container/ContainerRow";
import BetweenLocations from "./BetweenLocations";

const LocationField = 'idEventRoutes?[isStart&isEnd&latitude,longitude]';
const latLng = "latitude,longitude";

const Locations = ({updateFilterObject, filterObject}) => {
    let filteredLocations = filterObject.filters[LocationField]?.values;

    const onChangeLocation = (isStart, isEnd) => (location) => {
        const newElement = {
            isStart: isStart,
            isEnd: isEnd,
            [latLng]: location.lat + ',' + location.lng
        };
        if (location) {
            if (filteredLocations) {
                filteredLocations.push(newElement)
            } else {
                filteredLocations = [newElement]
            }
        }
        updateFilterObject(FilterOperator.NEAR_LOCATION, filteredLocations, LocationField, true);
    }

    return <Container>
        <ItemContainer>
            <AutocompleteLocation setCenter={onChangeLocation(true, false)}/>
        </ItemContainer>
        <BetweenLocations updateFilterObject={onChangeLocation(false, false)} filterObject={filterObject}/>
        <ItemContainer>
            <AutocompleteLocation setCenter={onChangeLocation(false, true)}/>
        </ItemContainer>
    </Container>
}

Locations.propTypes = {
    updateFilterObject: PropTypes.func.isRequired,
    filterObject: SearchObject.isRequired
}
export default Locations;