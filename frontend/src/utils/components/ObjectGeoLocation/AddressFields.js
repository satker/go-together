import React from 'react';
import * as PropTypes from "prop-types";
import {getAddress, getCity, getCountry, getState} from "./utils";

const AddressFields = ({response, onChange}) => {

    return <div className='flex-fill'>
        <div className="form-group">
            <label htmlFor="">Country</label>
            <input type="text"
                   name="state"
                   className="form-control"
                   readOnly="readOnly"
                   value={getCountry(response)}/>
        </div>
        <div className="form-group">
            <label htmlFor="">State</label>
            <input type="text"
                   name="state"
                   className="form-control"
                   readOnly="readOnly"
                   value={getState(response)}/>
        </div>
        <div className="form-group">
            <label htmlFor="">City</label>
            <input type="text"
                   name="city"
                   className="form-control"
                   readOnly="readOnly"
                   value={getCity(response)}/>
        </div>
        <div className="form-group">
            <label htmlFor="">Address</label>
            <input type="text"
                   name="address"
                   className="form-control"
                   readOnly="readOnly"
                   value={getAddress(response)}/>
        </div>
    </div>;
};

AddressFields.propTypes = {
    response: PropTypes.object.isRequired,
    onChange: PropTypes.func.isRequired
};

export default AddressFields;