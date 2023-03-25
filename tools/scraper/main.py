import logging

from scraper import web_scrap

log = logging.getLogger()


def configure_logging() -> None:
    """Set logging level and format."""
    logging.basicConfig(level=logging.DEBUG,
                        format='%(asctime)s - %(levelname)s - %(message)s')
    log.setLevel(logging.INFO)


def main() -> None:
    """Main scraper method. Initialize logging and config."""
    configure_logging()
    web_scrap()


if __name__ == '__main__':
    main()
