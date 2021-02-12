import React, {useState} from 'react';
import PropTypes from 'prop-types';

import AutocompleteLocation from "forms/utils/components/AutocompleteLocation";
import ItemContainer from "forms/utils/components/Container/ItemContainer";
import {Location} from "forms/utils/types";

const AutocompleteLocationField = ({name, placeholder, setValue, error, value}) => {
    const [currentValue, setCurrentValue] = useState();

    const onChange = (latLng, loc) => {
        let location = {
            ...value,
            address: loc.name,
            latitude: latLng.lat,
            longitude: latLng.lng,
            place: loc
        };
        setValue(name, location);
    };

    return <ItemContainer style={{height: 70}}>
        <AutocompleteLocation onChangeLocation={(place, {lat, lng}) => onChange({lat, lng}, place)}
                              placeholder={placeholder}
                              setValueCenter={setCurrentValue}
                              value={currentValue || (value && value.place)}
                              name='place'
                              error={error}
        />
    </ItemContainer>
};

AutocompleteLocationField.propTypes = {
    name: PropTypes.string.isRequired,
    placeholder: PropTypes.string,
    value: Location,
    setValue: PropTypes.func,
    error: PropTypes.string
};

export default AutocompleteLocationField;