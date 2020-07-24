import React from "react";
import PropTypes from "prop-types";
import {keys} from 'lodash';

import {connect} from "App/Context";

import AutocompleteLocation from "forms/utils/components/AutocompleteLocation";
import {FilterOperator} from "forms/utils/utils";
import {SearchObject} from "forms/utils/types";
import ItemContainer from "forms/utils/components/Container/ItemContainer";
import Container from "forms/utils/components/Container/ContainerRow";

import BetweenLocations from "./BetweenLocations";
import {setFilter} from "../../actions";

export const LocationField = 'idEventRoutes?[isStart&isEnd&latitude,longitude]';
export const latLng = "latitude,longitude";

const Locations = ({setFilter, filter}) => {
    function updatedLocations(locations, filteredLocations) {
        const updatedBetweenLocations = locations
            .filter(value => value.lat && value.lng)
            .map(value => ({
                isStart: false,
                isEnd: false,
                [latLng]: value.lat + ',' + value.lng
            }))
        filteredLocations = filteredLocations
            .filter(place => place.isStart || place.isEnd);
        filteredLocations = [...filteredLocations, ...updatedBetweenLocations];
        return filteredLocations;
    }

    const onChangeLocation = (isStart, isEnd) => (locations) => {
        const foundKey = keys(filter.filters)
            .find(filterKey => filterKey.startsWith(LocationField));

        let filteredLocations = foundKey ? filter.filters[foundKey]?.values : [];

        if (locations instanceof Array) {
            filteredLocations = updatedLocations(locations, filteredLocations);
        } else {
            const newElement = {
                isStart: isStart,
                isEnd: isEnd,
                [latLng]: locations.lat + ',' + locations.lng
            };
            if (filteredLocations) {
                filteredLocations = filteredLocations
                    .filter(place => !(place.isStart === isStart && place.isEnd === isEnd));
            }
            filteredLocations.push(newElement)
        }

        setFilter(FilterOperator.NEAR_LOCATION, filteredLocations, LocationField, true);
    }

    return <Container>
        <ItemContainer>
            <AutocompleteLocation
                setCenter={onChangeLocation(true, false)}
                placeholder='Start place'/>
        </ItemContainer>
        <BetweenLocations onChangeLocation={onChangeLocation(false, false)}/>
        <ItemContainer>
            <AutocompleteLocation
                setCenter={onChangeLocation(false, true)}
                placeholder='End place'/>
        </ItemContainer>
    </Container>
}

Locations.propTypes = {
    setFilter: PropTypes.func.isRequired,
    filter: SearchObject.isRequired
}

const mapStateToProps = state => ({
    filter: state.components.forms.events.filter.response
});

export default connect(mapStateToProps, {setFilter})(Locations);