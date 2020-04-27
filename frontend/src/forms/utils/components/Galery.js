import PropTypes from 'prop-types';
import React, {useState} from 'react';
import {css, StyleSheet} from 'aphrodite/no-important';
import Lightbox from 'react-images';
import CustomButton from "./CustomButton";

const Gallery = ({
                     images, heading, subheading, preventScroll,
                     showThumbnails, spinner, spinnerColor, spinnerSize, theme
                 }) => {
    const [lightboxIsOpen, setLightboxIsOpen] = useState(false);
    const [currentImage, setCurrentImage] = useState(0);

    let imagesToShow = [];
    images.map(photo => photo)
        .forEach(photo => imagesToShow.push({src: photo}));

    const openLightbox = (index, event) => {
        event.preventDefault();
        setCurrentImage(index);
        setLightboxIsOpen(true);
    };

    const openLightboxByButton = () => {
        setLightboxIsOpen(true);
    };

    const closeLightbox = () => {
        setCurrentImage(0);
        setLightboxIsOpen(false);
    };

    const gotoPrevious = () => {
        setCurrentImage(currentImage - 1);
    };

    const gotoNext = () => {
        setCurrentImage(currentImage + 1);
    };

    const gotoImage = (index) => {
        setCurrentImage(index);
    };

    const handleClickImage = () => {
        if (currentImage === imagesToShow.length - 1) return;

        gotoNext();
    };

    const renderGallery = () => {
        if (!imagesToShow) return;

        const gallery = imagesToShow.filter(i => i.useForDemo).map((obj, i) => {
            return (
                <a
                    href={obj.src}
                    className={css(classes.thumbnail, classes[obj.orientation])}
                    key={i}
                    onClick={(e) => openLightbox(i, e)}
                >
                    <img src={obj.thumbnail} className={css(classes.source)} alt={''}/>
                </a>
            );
        });

        return (
            <div className={css(classes.gallery)}>
                {gallery}
            </div>
        );
    };

    return (
        <div className="section">
            {heading && <h2>{heading}</h2>}
            {subheading && <p>{subheading}</p>}
            <CustomButton onClick={openLightboxByButton} text='See photos'/>
            {renderGallery()}
            <Lightbox
                currentImage={currentImage}
                images={imagesToShow}
                isOpen={lightboxIsOpen}
                onClickImage={handleClickImage}
                onClickNext={gotoNext}
                onClickPrev={gotoPrevious}
                onClickThumbnail={gotoImage}
                onClose={closeLightbox}
                preventScroll={preventScroll}
                showThumbnails={showThumbnails}
                spinner={spinner}
                spinnerColor={spinnerColor}
                spinnerSize={spinnerSize}
                theme={theme}
            />
        </div>
    );

};

Gallery.propTypes = {
    heading: PropTypes.string,
    images: PropTypes.array,
    showThumbnails: PropTypes.bool,
    subheading: PropTypes.string,
    isOpen: PropTypes.bool
};

export default Gallery;

const gutter = {
    small: 2,
    large: 4,
};
const classes = StyleSheet.create({
    gallery: {
        marginRight: -gutter.small,
        overflow: 'hidden',

        '@media (min-width: 500px)': {
            marginRight: -gutter.large,
        },
    },

    // anchor
    thumbnail: {
        boxSizing: 'border-box',
        display: 'block',
        float: 'left',
        lineHeight: 0,
        paddingRight: gutter.small,
        paddingBottom: gutter.small,
        overflow: 'hidden',

        '@media (min-width: 500px)': {
            paddingRight: gutter.large,
            paddingBottom: gutter.large,
        },
    },

    // orientation
    landscape: {
        width: '30%',
    },
    square: {
        paddingBottom: 0,
        width: '40%',

        '@media (min-width: 500px)': {
            paddingBottom: 0,
        },
    },

    // actual <img />
    source: {
        border: 0,
        display: 'block',
        height: 'auto',
        maxWidth: '100%',
        width: 'auto',
    },
});