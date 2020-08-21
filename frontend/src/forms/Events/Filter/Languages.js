import React, {useEffect} from 'react';
import PropTypes from "prop-types";

import {connect} from "App/Context";

import MultipleSelectBox from "forms/utils/components/MultipleSelectBox";
import LoadableContent from "forms/utils/components/LoadableContent";
import {FilterOperator} from "forms/utils/utils";

import {getLanguages, setFilter} from "../actions";
import {ResponseData} from "forms/utils/types";
import {keys} from "lodash";

const LANGUAGES_FIELD = "author?languages.id";

const Languages = ({languages, getLanguages, setFilter, chooseLanguages}) => {
    useEffect(() => {
        getLanguages();
    }, [getLanguages]);

    const onChange = (languages) => {
        let searchLanguages = null;
        if (languages.length !== 0) {
            searchLanguages = [{
                "id": languages.map(language => language.id)
            }];
        }
        setFilter(FilterOperator.IN, searchLanguages, LANGUAGES_FIELD, languages.length);
    }

    return <LoadableContent loadableData={languages}>
        <MultipleSelectBox onChange={onChange}
                           value={chooseLanguages}
                           optionsSimple={languages.response.result}
                           label='Languages'/>
    </LoadableContent>;
};

Languages.propTypes = {
    setFilter: PropTypes.func.isRequired,
    getLanguages: PropTypes.func.isRequired,
    languages: ResponseData
}

const mapStateToProps = state => {
    const languages = state.components.forms.events.languages;
    const filters = state.components.forms.events.filter.response.filters;
    const filtersName = keys(filters)
        .find(keyFilter => keyFilter.startsWith(LANGUAGES_FIELD));
    const filterLanguages = filters[filtersName]?.values[0].id || [];
    const chooseLanguages = filterLanguages
        .map(languageId => ({
            id: languageId,
            name: languages.response?.result.find(language => language.id === languageId)
        }).name)

    return ({
        languages,
        chooseLanguages
    });
}

export default connect(mapStateToProps, {getLanguages, setFilter})(Languages);