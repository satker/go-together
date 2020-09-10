import React, {useEffect, useState} from "react";
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

const LOCATION_FIELD = 'idEventRoutes?locations.[isStart&isEnd&latitude,longitude]';
const LAT_LNG = "latitude,longitude";

export const getChangedRoutes = (filters, routes, filterRoutes) => {
    const locationsKey = keys(filters?.filters)?.find(key => key.startsWith(LOCATION_FIELD));
    const searchedBetweenLatLng = filters?.filters[locationsKey]?.values
        .filter(filterRoutes)
        .map(value => value[LAT_LNG])
        .map(value => value.split(","))
        .map(value => ({lat: value[0], lng: value[1]}));
    return [...routes.filter(route =>
        searchedBetweenLatLng &&
        route?.lat &&
        route?.lng &&
        searchedBetweenLatLng.find(searchedLatLng => route.lat.toString() === searchedLatLng.lat
            && searchedLatLng.lng === route.lng.toString()))];
};

const Locations = ({setFilter, filter}) => {
    const [firstRoute, setFirstRoute] = useState(null);
    const [lastRoute, setLastRoute] = useState(null);

    useEffect(() => {
        if (filter?.filters) {
            const newFirstRoute = getChangedRoutes(filter, [firstRoute], (value) => !value.isEnd && value.isStart);
            setFirstRoute(newFirstRoute[0])
        }
    }, [filter]);

    useEffect(() => {
        if (filter?.filters) {
            const newLastRoute = getChangedRoutes(filter, [lastRoute], (value) => value.isEnd && !value.isStart);
            setLastRoute(newLastRoute[0])
        }
    }, [filter]);

    const updatedLocations = (locations, filteredLocations) => {
        const updatedBetweenLocations = locations
            .filter(value => value.lat && value.lng)
            .map(value => ({
                isStart: false,
                isEnd: false,
                [LAT_LNG]: value.lat + ',' + value.lng
            }))
        filteredLocations = filteredLocations
            .filter(place => place.isStart || place.isEnd);
        filteredLocations = [...filteredLocations, ...updatedBetweenLocations];
        return filteredLocations;
    }

    const onChangeLocation = (isStart, isEnd) => (locations) => {
        const foundKey = keys(filter.filters)
            .find(filterKey => filterKey.startsWith(LOCATION_FIELD));

        let filteredLocations = foundKey ? filter.filters[foundKey]?.values : [];

        if (locations instanceof Array) {
            filteredLocations = updatedLocations(locations, filteredLocations);
        } else {
            if (isEnd) {
                setLastRoute(locations)
            } else {
                setFirstRoute(locations);
            }
            if (!locations.lat || !locations.lng) {
                return;
            } else {
                const newElement = {
                    isStart: isStart,
                    isEnd: isEnd,
                    [LAT_LNG]: locations.lat + ',' + locations.lng
                };
                if (filteredLocations) {
                    filteredLocations = filteredLocations
                        .filter(place => !(place.isStart === isStart && place.isEnd === isEnd));
                }
                filteredLocations.push(newElement);
            }
        }

        setFilter(FilterOperator.NEAR_LOCATION, filteredLocations, LOCATION_FIELD, filteredLocations.length);
    }

    return <Container>
        <ItemContainer>
            <AutocompleteLocation
                value={firstRoute?.value}
                setValueCenter={onChangeLocation(true, false)}
                placeholder='Start place'/>
        </ItemContainer>
        <BetweenLocations onChangeLocation={onChangeLocation(false, false)}/>
        <ItemContainer>
            <AutocompleteLocation
                value={lastRoute?.value}
                setValueCenter={onChangeLocation(false, true)}
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