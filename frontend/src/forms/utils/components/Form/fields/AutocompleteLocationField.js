import React from 'react';
import PropTypes from 'prop-types';

import AutocompleteLocation from "forms/utils/components/AutocompleteLocation";
import ItemContainer from "../../Container/ItemContainer";

const AutocompleteLocationField = ({name, placeholder, setValue, error, value}) => {
    const onChange = (latLng, loc) => {
        let location = {
            ...value,
            locations: [{
                ...value.locations[0],
                id: null,
                place: loc,
                address: loc.name,
                latitude: latLng.lat,
                longitude: latLng.lng
            }]
        };
        setValue(name, location);
    };
    return <ItemContainer style={{height: 70}}>
        <AutocompleteLocation onChangeLocation={(place, {lat, lng}) => onChange({lat, lng}, place)}
                              placeholder={placeholder}
                              value={value && value.locations[0].place}
                              name='place'
                              error={error}
        />
    </ItemContainer>
};

AutocompleteLocationField.propTypes = {
    value: PropTypes.array
}

export default AutocompleteLocationField;