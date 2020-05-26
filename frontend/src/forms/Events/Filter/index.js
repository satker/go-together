import React, {useState} from 'react';
import PropTypes from "prop-types";

import {AutosuggestionLocations} from "forms/utils/components/Autosuggestion";
import CheckInOutDates from 'forms/utils/components/CheckInOutDates'
import {FORM_DTO, LOCATION_SERVICE_URL} from 'forms/utils/constants'
import {SearchObject} from "forms/utils/types";
import {FilterOperator} from "forms/utils/utils";
import CustomButton from "forms/utils/components/CustomButton";
import {connect} from "App/Context";

import {setArrivalDate, setDepartureDate, setPage} from "../actions";
import AuthorFilters from "./AuthorFilters";

const Filter = ({
                    filterObject, updateFilterObject
                }) => {
    const [location, setLocation] = useState(null);

    const clearFilters = () => {
        updateFilterObject({...FORM_DTO("event")});
        setPage(0);
    };
    const onChangeDate = (searchField, filterOperation) => (date) => {
        updateFilterObject(filterOperation, date, searchField);
    }

    const startDate = filterObject.filters['startDate'];
    const endDate = filterObject.filters['endDate'];

    return <div className='container-search-events'>
        <div className='flex'>
            <AutosuggestionLocations formId='search_form_'
                                     setResult={setLocation}
                                     placeholder={'Search a location (CITY,COUNTRY)'}
                                     url={LOCATION_SERVICE_URL + '/locations'}
                                     urlParam={'name'}/>
        </div>
        <div className='flex margin-left-custom'>
            <CheckInOutDates startDate={startDate && startDate.values[0]?.id}
                             endDate={endDate && endDate.values[0]?.id}
                             setStartDate={onChangeDate('startDate', FilterOperator.START_DATE)}
                             setEndDate={onChangeDate('endDate', FilterOperator.END_DATE)}/>
        </div>
        <AuthorFilters updateFilterObject={updateFilterObject}/>
        <div className='flex margin-left-custom'>
            <CustomButton onClick={clearFilters} color="primary" text='Clear'/>
        </div>
    </div>
};

Filter.propTypes = {
    filterObject: SearchObject.isRequired,
    updateFilterObject: PropTypes.func.isRequired,
};

export default connect(null,
    {setPage, setArrivalDate, setDepartureDate})(Filter);