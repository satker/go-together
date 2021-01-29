import React from 'react';
import PropTypes from "prop-types";

import {connect} from "App/Context";

import {FilterOperator} from "forms/utils/utils";
import CheckInOutDates from "forms/utils/components/CheckInOutDates";

import {setFilter} from "../actions";
import {SearchObject} from "forms/utils/types";

const START_DATE = 'startDate';
const END_DATE = 'endDate';

const Dates = ({filter, setFilter}) => {
    const onChangeDate = (searchField, filterOperation) => (date) => {
        if (date) {
            const values = [{
                [searchField]: {
                    filterType: filterOperation.operator,
                    value: date
                }
            }];
            setFilter(values, searchField);
        }
    }

    const startDate = filter.filters[START_DATE];
    const endDate = filter.filters[END_DATE];

    return <CheckInOutDates startDate={startDate && startDate.values[0]?.startDate.value}
                            endDate={endDate && endDate.values[0]?.endDate.value}
                            setStartDate={onChangeDate(START_DATE, FilterOperator.START_DATE)}
                            setEndDate={onChangeDate(END_DATE, FilterOperator.END_DATE)}/>;
};

Dates.propTypes = {
    setFilter: PropTypes.func.isRequired,
    filter: SearchObject.isRequired
}

const mapStateToProps = state => ({
    filter: state.components.forms.events.filter.response
});

export default connect(mapStateToProps, {setFilter})(Dates);

