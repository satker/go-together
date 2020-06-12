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
    const onChangeLocation = (isStart, isEnd) => (locations) => {
        const foundKey = keys(filter.filters)
            .find(filterKey => filterKey.startsWith(LocationField));

        let filteredLocations = foundKey ? filter.filters[foundKey]?.values : [];

        if (locations instanceof Array) {
            const updatedBetweenLocations = locations
                .filter(value => value.lat && value.lng)
                .map(value => ({
                    isStart: false,
                    isEnd: false,
                    [latLng]: value.lat + ',' + value.lng
                }))
            filteredLocations = filteredLocations
                .filter(location => location.isStart || location.isEnd);
            filteredLocations = [...filteredLocations, ...updatedBetweenLocations];
        } else {
            const newElement = {
                isStart: isStart,
                isEnd: isEnd,
                [latLng]: locations.lat + ',' + locations.lng
            };
            if (filteredLocations) {
                filteredLocations = filteredLocations
                    .filter(location => !(location.isStart === isStart && location.isEnd === isEnd));
            }
            filteredLocations.push(newElement)
        }
        console.log(filteredLocations)
        setFilter(FilterOperator.NEAR_LOCATION, filteredLocations, LocationField, true);
    }

    return <Container>
        <ItemContainer>
            <AutocompleteLocation
                setCenter={onChangeLocation(true, false)}
                placeholder='Start location'/>
        </ItemContainer>
        <BetweenLocations onChangeLocation={onChangeLocation(false, false)}/>
        <ItemContainer>
            <AutocompleteLocation
                setCenter={onChangeLocation(false, true)}
                placeholder='End location'/>
        </ItemContainer>
    </Container>
}

Locations.propTypes = {
    setFilter: PropTypes.func.isRequired,
    filter: SearchObject.isRequired
}

const mapStateToProps = () => state => ({
    filter: state.components.forms.events.filter.response
});

export default connect(mapStateToProps, {setFilter})(Locations);