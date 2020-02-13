import React from 'react';
import * as PropTypes from "prop-types";

const AddressFields = ({response, onChange}) => {

    const getAddress = () => {
        const address = response.results[0].formatted_address;
        onChange('address', address);
        return address;
    };

    const getCity = (addressArray) => {
        let city = '';
        for (let i = 0; i < addressArray.length; i++) {
            if (addressArray[i].types[0] && 'administrative_area_level_2' === addressArray[i].types[0]) {
                city = addressArray[i].long_name;
                onChange('location.name', city);
                return city;
            }
        }
    };

    const getCountry = (addressArray) => {
        let country = '';
        for (let i = 0; i < addressArray.length; i++) {
            for (let i = 0; i < addressArray.length; i++) {
                if (addressArray[i].types[0] && 'country' === addressArray[i].types[0]) {
                    country = addressArray[i].long_name;
                    onChange('location.country.name', country);
                    return country;
                }
            }
        }
    };

    const getState = (addressArray) => {
        let state = '';
        for (let i = 0; i < addressArray.length; i++) {
            for (let i = 0; i < addressArray.length; i++) {
                if (addressArray[i].types[0] && 'administrative_area_level_1' === addressArray[i].types[0]) {
                    state = addressArray[i].long_name;
                    onChange('location.state', state);
                    return state;
                }
            }
        }
    };

    const addressArray = response.results[0].address_components;

    return <div className='flex-fill'>
        <div className="form-group">
            <label htmlFor="">Country</label>
            <input type="text"
                   name="state"
                   className="form-control"
                   readOnly="readOnly"
                   value={getCountry(addressArray)}/>
        </div>
        <div className="form-group">
            <label htmlFor="">State</label>
            <input type="text"
                   name="state"
                   className="form-control"
                   readOnly="readOnly"
                   value={getState(addressArray)}/>
        </div>
        <div className="form-group">
            <label htmlFor="">City</label>
            <input type="text"
                   name="city"
                   className="form-control"
                   readOnly="readOnly"
                   value={getCity(addressArray)}/>
        </div>
        <div className="form-group">
            <label htmlFor="">Address</label>
            <input type="text"
                   name="address"
                   className="form-control"
                   readOnly="readOnly"
                   value={getAddress()}/>
        </div>
    </div>;
};

AddressFields.propTypes = {
    response: PropTypes.object.isRequired,
    onChange: PropTypes.func.isRequired
};

export default AddressFields;