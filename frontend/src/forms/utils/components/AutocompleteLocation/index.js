import React, {useEffect, useRef} from 'react';
import PropTypes from "prop-types";
import LabeledInput from "../LabeledInput";

const AutocompleteLocation = ({google, setCenter}) => {
    const autocompleteInput = useRef();

    useEffect(() => {
        const options = {
            types: ['(cities)'],
        };

        const autocompleteCustom = new google.maps.places.Autocomplete(autocompleteInput.current, options);

        autocompleteCustom.setFields(['address_components', 'formatted_address', 'geometry']);

        autocompleteCustom.addListener('place_changed', () => {
            const addressObject = autocompleteCustom.getPlace();
            setCenter({lat: addressObject.geometry.location.lat(), lng: addressObject.geometry.location.lng()});
        });
    }, [autocompleteInput, google, setCenter]);

    return <LabeledInput
        id='autocomplete'
        inputRef={autocompleteInput}
        label='Location'/>;
};

AutocompleteLocation.propTypes = {
    google: PropTypes.object.isRequired,
    setCenter: PropTypes.func.isRequired
};

export default AutocompleteLocation;