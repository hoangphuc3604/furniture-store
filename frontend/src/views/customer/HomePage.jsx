import React, { useEffect } from "react";
import ProductCard from "../util/ProductCard";
import Carousel from "react-multi-carousel";
import "react-multi-carousel/lib/styles.css";
import { Link, useNavigate } from "react-router-dom";
import "../../carousel.css";
import "react-responsive-carousel/lib/styles/carousel.min.css";
import { useDispatch, useSelector } from "react-redux";
import { get_categories } from "../../store/Reducers/categoryReducer";
import Loading from "./../components/Loading";

const Homepage = () => {
  const { categories, loader } = useSelector((state) => state.categories);

  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(get_categories());
  }, [dispatch]);

  const categoryImages = [
    { src: "/images/category/Chair.jpg", name: "Chair" },
    { src: "/images/category/Sofa.jpg", name: "Sofa" },
    { src: "/images/category/Cabinet.jpg", name: "Cabinet" },
    { src: "/images/category/Table.jpg", name: "Table" },
    { src: "/images/category/Bed.jpg", name: "Bed" },
  ];

  const mergedCategories = categories.map((category) => {
    const image = categoryImages.find(
      (cat) => cat.name === category.category_name
    );
    return {
      ...category,
      src: image ? image.src : "/images/category/Chair.jpg",
    };
  });

  const responsiveOne = {
    all: {
      breakpoint: { max: 3000, min: 0 },
      items: 1,
      slidesToSlide: 1, // optional, default to 1.
    },
  };

  const responsiveTwo = {
    desktop: {
      breakpoint: { max: 3000, min: 1024 },
      items: 3,
      slidesToSlide: 3, // optional, default to 1.
    },
    tablet: {
      breakpoint: { max: 1024, min: 464 },
      items: 2,
      slidesToSlide: 2, // optional, default to 1.
    },
    mobile: {
      breakpoint: { max: 464, min: 0 },
      items: 1,
      slidesToSlide: 1, // optional, default to 1.
    },
  };

  if (loader) {
    return <Loading />;
  }

  return (
    <div className="px-2 lg:px-7 pt-5">
      <Carousel
        showThumbs={false}
        autoPlay
        infiniteLoop
        responsive={responsiveOne}
      >
        <div className="relative">
          <img src="/images/carousel-1.jpg" alt="Featured 1" />
          <Link to="/customer/product-list" className="legend">
            <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline absolute bottom-4 left-1/2 transform -translate-x-1/2">
              Shop Now
            </button>
          </Link>
        </div>
        <div className="relative">
          <img src="/images/carousel-2.jpg" alt="Featured 2" />
          <Link to="/customer/product-list" className="legend">
            <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline absolute bottom-4 left-1/2 transform -translate-x-1/2">
              Shop Now
            </button>
          </Link>
        </div>
        <div className="relative">
          <img src="/images/carousel-3.jpg" alt="Featured 3" />
          <Link to="/customer/product-list" className="legend">
            <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline absolute bottom-4 left-1/2 transform -translate-x-1/2">
              Shop Now
            </button>
          </Link>
        </div>
      </Carousel>

      <section className="m-5 pt-5">
        <h1 className="text-xl font-bold mb-3">Feedback</h1>
        <div className="max-w-lg mx-auto">
          <form>
            <div className="mb-4">
              <label
                className="block text-gray-700 text-sm font-bold mb-2"
                htmlFor="feedback"
              >
                Your Feedback
              </label>
              <textarea
                id="feedback"
                name="feedback"
                rows="4"
                className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                placeholder="Enter your feedback here..."
              ></textarea>
            </div>
            <div className="flex items-center justify-between">
              <button
                className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
                type="button"
              >
                Submit
              </button>
            </div>
          </form>
        </div>
      </section>

      <section className="m-5 pt-5 pb-5">
        <h1 className="text-xl font-bold mb-3">Categories</h1>
        <div className="mt-8">
          <Carousel
            showThumbs={false}
            autoPlay
            infiniteLoop
            responsive={responsiveTwo}
          >
            {mergedCategories.map((image, index) => {
              return (
                <div key={index} className="fixed-size relative">
                  <img src={image.src} alt={`Featured ${index + 1}`} />
                  <Link
                    to={`/customer/product-list?category=${image.category_name}`}
                    className="legend"
                  >
                    <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline absolute bottom-4 left-1/2 transform -translate-x-1/2">
                      Shop Now
                    </button>
                  </Link>
                </div>
              );
            })}
          </Carousel>
        </div>
      </section>
    </div>
  );
};

export default Homepage;
