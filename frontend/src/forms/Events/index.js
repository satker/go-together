import React, {useEffect, useState} from "react";
import Filter from "./Filter";
import {FORM_DTO, SEARCH_OBJECT_DEFAULT} from '../utils/constants'
import {get, isEqual, set} from "lodash";
import GroupItems from "../utils/components/CardItems";
import {connect} from "../../App/Context";
import {postFindEvents} from "./actions";
import Container from "../utils/components/Container/ContainerRow";
import LoadableContent from "../utils/components/LoadableContent";
import {postLikes} from "../utils/components/Event/EventLikes/actions";
import CustomPagination from "../utils/components/Pagination";

const Events = ({pageSize, postFindEvents, findEvents, postLikes}) => {
    const [searchObject, setSearchObject] = useState({...SEARCH_OBJECT_DEFAULT});
    const [page, setPage] = useState(1);
    const [filterObject, setFilterObject] = useState({...FORM_DTO("apartment.id")});

    useEffect(() => {
        filterObject.page.size = pageSize;
    }, [filterObject, pageSize]);

    useEffect(() => {
        if (findEvents.response.result && findEvents.response.result?.length) {
            const eventIds = findEvents.response.result.map(event => event.id);
            postLikes(eventIds);
        }
    }, [findEvents, postLikes]);

    useEffect(() => {
        postFindEvents(filterObject);
    }, [postFindEvents, filterObject]);

    const onClickNextPage = page => {
        filterObject.page.page = page - 1;
        setPage(page);
        postFindEvents(filterObject);
    };

    const onDelete = () => null;

    const onChangeSearchObject = (field, value) => {
        const object = {...searchObject};
        if (!isEqual(get(object, field), value)) {
            set(object, field, value);
            setSearchObject(object);
        }
    };

    const onClearSearchObject = () => setSearchObject({...SEARCH_OBJECT_DEFAULT});

    const pageCount = findEvents.response.page ?
        findEvents.response.page.totalSize / findEvents.response.page.size : 0;

    return <Container>
        <Container className='search-container'>
            <Filter filterObject={filterObject}
                    setFilterObject={setFilterObject}
                    searchObject={searchObject}
                    onChangeSearchObject={onChangeSearchObject}
                    onClearSearchObject={onClearSearchObject}
            />
        </Container>
        <Container className='events-container'>
            <LoadableContent loadableData={findEvents}>
                <GroupItems
                    onDelete={onDelete}
                    items={findEvents.response.result}
                    isEvents
                />
            </LoadableContent>
        </Container>
        {pageCount <= 1 || <CustomPagination pageCount={pageCount}
                                             page={page}
                                             setPage={onClickNextPage}/>}
    </Container>;
};

const mapStateToProps = () => state => ({
    pageSize: state.pageSize.value,
    findEvents: state.components.forms.events.findEvents,
});

export default connect(mapStateToProps,
    {postFindEvents, postLikes})
(Events);