import React, {useEffect, useState} from "react";
import PropTypes from "prop-types";

import {connect} from "App/Context";

import GroupItems from "forms/utils/components/CardItems";
import LoadableContent from "forms/utils/components/LoadableContent";
import {getEventsLikes} from "forms/utils/components/Event/EventLikes/actions";
import CustomPagination from "forms/utils/components/Pagination";
import {ResponseData, SearchObject} from "forms/utils/types";
import CustomButton from "forms/utils/components/CustomButton";

import {postFindEvents} from "./actions";
import Map from "./Map";

const Events = ({pageSize, postFindEvents, findEvents, getEventsLikes, filter}) => {
    const [page, setPage] = useState(1);
    const [isMap, setIsMap] = useState(false);

    useEffect(() => {
        filter.page.size = pageSize;
    }, [filter, pageSize]);

    useEffect(() => {
        if (findEvents.response.result && findEvents.response.result?.length) {
            const eventIds = findEvents.response.result.map(event => event.id);
            getEventsLikes(eventIds);
        }
    }, [findEvents, getEventsLikes]);

    useEffect(() => {
        postFindEvents(filter);
    }, [postFindEvents, filter]);

    const onClickNextPage = page => {
        filter.page.page = page - 1;
        setPage(page);
        postFindEvents(filter);
    };

    const onDelete = () => null;

    const pageCount = findEvents.response.page ?
        findEvents.response.page.totalSize / findEvents.response.page.size : 0;

    return <>
        <CustomButton onClick={() => setIsMap(!isMap)} text={isMap ? 'Hide map' : 'Show map'}/>
        <LoadableContent loadableData={findEvents}>
            {isMap ?
                <Map events={findEvents.response.result}/>
                : <GroupItems
                    onDelete={onDelete}
                    items={findEvents.response.result}
                    isEvents/>}
        </LoadableContent>
        {pageCount <= 1 || <CustomPagination pageCount={pageCount}
                                             page={page}
                                             setPage={onClickNextPage}/>}
    </>;
};

Events.propTypes = {
    postFindEvents: PropTypes.func.isRequired,
    postLikes: PropTypes.func.isRequired,
    filter: SearchObject.isRequired,
    findEvents: ResponseData,
    pageSize: PropTypes.number
}

const mapStateToProps = state => ({
    pageSize: state.pageSize.value,
    findEvents: state.components.forms.events.findEvents,
    filter: state.components.forms.events.filter.response
});

export default connect(mapStateToProps,
    {postFindEvents, getEventsLikes})
(Events);