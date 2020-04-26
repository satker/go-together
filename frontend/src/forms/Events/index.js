import React, {useEffect, useState} from "react";
import SearchForm from "./SearchForm";
import ReactPaginate from 'react-paginate';
import {FORM_DTO, SEARCH_OBJECT_DEFAULT} from '../utils/constants'
import './styles.css'
import {get, isEqual, set} from "lodash";
import GroupItems from "../utils/components/CardItems";
import {connect} from "../../App/Context";
import {postFindEvents} from "./actions";
import {FORM_ID} from "./constants";
import Container from "../utils/components/Container/ContainerRow";
import LoadableContent from "../utils/components/LoadableContent";
import {postLikes} from "../utils/components/Event/EventLikes/actions";

const Events = ({pageSize, postFindEvents, setEventId, findEvents, postLikes}) => {
    const [searchObject, setSearchObject] = useState({...SEARCH_OBJECT_DEFAULT});
    const [filterObject, setFilterObject] = useState({...FORM_DTO("apartment.id")});

    useEffect(() => {
        filterObject.page.size = pageSize;
    }, [filterObject, pageSize]);

    useEffect(() => {
        console.log(findEvents.response.result);
        if (findEvents.response.result && findEvents.response.result?.length) {
            const eventIds = findEvents.response.result.map(event => event.id);
            postLikes(eventIds);
        }
    }, [findEvents, postLikes]);

    useEffect(() => {
        postFindEvents(filterObject);
    }, [postFindEvents, filterObject]);

    const onClickNextPage = page => {
        filterObject.page.page = page.selected;
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

    return <Container formId={FORM_ID}>
        <Container className='search-container'>
            <SearchForm filterObject={filterObject}
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
        <br/>
        {pageCount <= 1 || <Container sm="12" md={{size: 6, offset: 3}}>
            <div className="react-paginate">
                <ReactPaginate
                    previousLabel={"← Previous"}
                    nextLabel={"Next →"}
                    breakLabel={'...'}
                    breakClassName={'break-me'}
                    pageCount={pageCount}
                    marginPagesDisplayed={2}
                    pageRangeDisplayed={5}
                    onPageChange={onClickNextPage}
                    containerClassName={'pagination'}
                    subContainerClassName={'pages pagination'}
                    activeClassName={'active'}
                />
            </div>
        </Container>
        }
    </Container>;
};

const mapStateToProps = () => state => ({
    pageSize: state.pageSize,
    findEvents: state.components.forms.events.findEvents,
});

export default connect(mapStateToProps,
    {postFindEvents, postLikes})
(Events)(FORM_ID);