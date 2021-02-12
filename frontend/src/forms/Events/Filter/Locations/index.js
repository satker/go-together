import React, {useEffect, useState} from "react";
import PropTypes from "prop-types";
import {keys} from 'lodash';

import {connect} from "App/Context";

import AutocompleteLocation from "forms/utils/components/AutocompleteLocation";
import {FilterOperator} from "forms/utils/utils";
import {SearchObject} from "forms/utils/types";
import ItemContainer from "forms/utils/components/Container/ItemContainer";

import BetweenLocations from "./BetweenLocations";
import {setFilter} from "../../actions";

const LAT_LNG = "latitude,longitude";
const LOCATION_FIELD = 'routes?infoRoutes.[isStart&isEnd&locationRoutes?latitude,longitude]';

export const getChangedRoutes = (filter, routes, filterRoutes) => {
    const locationsKey = keys(filter?.filters)?.find(key => key.startsWith(LOCATION_FIELD));
    const searchedBetweenLatLng = filter?.filters[locationsKey]?.values
        .filter(filterRoutes)
        .map(value => value[LAT_LNG].value)
        .map(value => value.split(","))
        .map(value => ({lat: value[0], lng: value[1]}));
    return [...routes.filter(route =>
        searchedBetweenLatLng &&
        route?.lat.value &&
        route?.lng.value &&
        searchedBetweenLatLng.find(searchedLatLng => route.lat.toString() === searchedLatLng.lat
            && searchedLatLng.lng === route.lng.toString()))];
};

const newFilterValue = (isStart, isEnd, location) => ({
    isStart: {
        filterType: FilterOperator.EQUAL.operator,
        value: isStart
    },
    isEnd: {
        filterType: FilterOperator.EQUAL.operator,
        value: isEnd
    },
    [LAT_LNG]: {
        filterType: FilterOperator.NEAR_LOCATION.operator,
        value: location
    }
});

const Locations = ({setFilter, filter}) => {
    const [firstRoute, setFirstRoute] = useState(null);
    const [lastRoute, setLastRoute] = useState(null);

    useEffect(() => {
        if (filter?.filters) {
            const newFirstRoute = getChangedRoutes(filter, [firstRoute], (value) => !value.isEnd.value && value.isStart.value);
            setFirstRoute(newFirstRoute[0])
        }
    }, [filter]);

    useEffect(() => {
        if (filter?.filters) {
            const newLastRoute = getChangedRoutes(filter, [lastRoute], (value) => value.isEnd && !value.isStart);
            setLastRoute(newLastRoute[0])
        }
    }, [filter]);

    const updateBetweenLocations = (locations, filteredLocations) => {
        const updatedBetweenLocations = locations
            .filter(value => value.lat && value.lng)
            .map(value => newFilterValue(false, false, value.lat + ',' + value.lng));
        return [...filteredLocations.filter(place => place.isStart.value || place.isEnd.value),
            ...updatedBetweenLocations];
    }

    const onChangeLocation = (isStart, isEnd) => (locations) => {
        const foundKey = keys(filter.filters)
            .find(filterKey => filterKey.startsWith(LOCATION_FIELD));

        let filteredLocations = foundKey ? filter.filters[foundKey]?.values : [];

        if (locations instanceof Array) {
            filteredLocations = updateBetweenLocations(locations, filteredLocations);
        } else {
            if (isEnd) {
                setLastRoute(locations)
            } else {
                setFirstRoute(locations);
            }
            if (!locations.lat || !locations.lng) {
                if (locations.value.name === '' && filteredLocations) {
                    filteredLocations = filteredLocations
                        .filter(place => !(place.isStart.value === isStart && place.isEnd.value === isEnd));
                    setFilter(filteredLocations, LOCATION_FIELD, filteredLocations.length);
                }
                return;
            } else {
                const newElement = newFilterValue(isStart, isEnd, locations.lat + ',' + locations.lng);

                if (filteredLocations) {
                    filteredLocations = filteredLocations
                        .filter(place => !(place.isStart.value === isStart && place.isEnd.value === isEnd));
                }
                filteredLocations.push(newElement);
            }
        }

        setFilter(filteredLocations, LOCATION_FIELD, filteredLocations.length);
    }

    return <>
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
    </>
}

Locations.propTypes = {
    setFilter: PropTypes.func.isRequired,
    filter: SearchObject.isRequired
}

const mapStateToProps = state => ({
    filter: state.components.forms.events.filter.response
});

export default connect(mapStateToProps, {setFilter})(Locations);