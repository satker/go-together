import React, {useEffect, useRef} from 'react';
import PropTypes from "prop-types";

import {DEFAULT_COUNTRY, DEFAULT_LOCATION, DEFAULT_ROUTE} from "forms/utils/constants";
import {getCity, getCountry, getState} from "forms/utils/components/ObjectGeoLocation/utils";
import LabeledInput from "forms/utils/components/LabeledInput";
import {onChange} from "forms/utils/utils";

const google = window.google;

const AutocompleteLocation = ({setCenter, onChangeLocation}) => {
    const autocompleteInput = useRef();

    const getLocation = (paths, values) => {
        let newLocation = {...DEFAULT_ROUTE};
        newLocation = {...DEFAULT_LOCATION};
        newLocation.country = {...DEFAULT_COUNTRY};
        onChange(newLocation, result => newLocation = result)(paths, values);
        return newLocation;
    };

    useEffect(() => {
        const options = {
            types: ['(cities)'],
        };

        const autocompleteCustom = new google.maps.places.Autocomplete(autocompleteInput.current, options);

        autocompleteCustom.setFields(['address_components', 'formatted_address', 'geometry']);

        autocompleteCustom.addListener('place_changed', () => {
            const addressObject = autocompleteCustom.getPlace();
            if (onChangeLocation) {
                const newLocation = getLocation(['name', 'country.name', 'state'],
                    [getCity(addressObject), getCountry(addressObject), getState(addressObject)]);
                onChangeLocation(newLocation);
            } else if (setCenter) {
                setCenter({lat: addressObject.geometry.location.lat(), lng: addressObject.geometry.location.lng()});
            }
        });
    }, [autocompleteInput, google, setCenter]);

    return <LabeledInput
        id='autocomplete'
        inputRef={autocompleteInput}
        label='Location'/>;
};

AutocompleteLocation.propTypes = {
    setCenter: PropTypes.func,
    onChangeLocation: PropTypes.func
};

export default AutocompleteLocation;