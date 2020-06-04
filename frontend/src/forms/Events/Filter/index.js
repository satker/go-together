import React from 'react';
import PropTypes from "prop-types";

import CheckInOutDates from 'forms/utils/components/CheckInOutDates'
import {FORM_DTO} from 'forms/utils/constants'
import {SearchObject} from "forms/utils/types";
import {FilterOperator} from "forms/utils/utils";
import CustomButton from "forms/utils/components/CustomButton";
import Container from "forms/utils/components/Container/ContainerRow";
import ItemContainer from "forms/utils/components/Container/ItemContainer";
import {connect} from "App/Context";

import {setArrivalDate, setDepartureDate, setPage} from "../actions";
import AuthorFilters from "./AuthorFilters";
import Locations from "./Locations";

const Filter = ({
                    filterObject, updateFilterObject
                }) => {
    const clearFilters = () => {
        updateFilterObject({...FORM_DTO("event")});
        setPage(0);
    };
    const onChangeDate = (searchField, filterOperation) => (date) => {
        let values = null;
        if (date) {
            values = [{
                [searchField]: date
            }]
        }
        updateFilterObject(filterOperation, values, searchField);
    }

    const startDate = filterObject.filters['startDate'];
    const endDate = filterObject.filters['endDate'];

    return <Container>
        <Locations updateFilterObject={updateFilterObject} filterObject={filterObject}/>
        <ItemContainer>
            <CheckInOutDates startDate={startDate && startDate.values[0]?.startDate}
                             endDate={endDate && endDate.values[0]?.endDate}
                             setStartDate={onChangeDate('startDate', FilterOperator.START_DATE)}
                             setEndDate={onChangeDate('endDate', FilterOperator.END_DATE)}/>
        </ItemContainer>
        <AuthorFilters updateFilterObject={updateFilterObject}/>
        <ItemContainer>
            <CustomButton onClick={clearFilters} color="primary" text='Clear'/>
        </ItemContainer>
    </Container>
};

Filter.propTypes = {
    filterObject: SearchObject.isRequired,
    updateFilterObject: PropTypes.func.isRequired,
};

export default connect(null,
    {setPage, setArrivalDate, setDepartureDate})(Filter);