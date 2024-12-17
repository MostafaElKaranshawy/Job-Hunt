export const truncateDescription = (description, limit = 100) => {
    return description.length > limit
        ? description.substring(0, limit) + "..."
        : description;
};
export const calculateRelativeTime = (postedAt) => {
    const now = new Date();
    const postedDate = new Date(postedAt);
    const diffInSeconds = Math.abs(Math.floor((now - postedDate) / 1000));
    if (diffInSeconds < 3600) {
        const minutes = Math.floor(diffInSeconds / 60);
        return `🕛 ${minutes} minute${minutes > 1 ? "s" : ""} ago`;
    } else if (diffInSeconds < 86400) {
        const hours = Math.floor(diffInSeconds / 3600);
        return `🕛 ${hours} hour${hours > 1 ? "s" : ""} ago`;
    } else if (diffInSeconds < 604800) {
        const days = Math.floor(diffInSeconds / 86400);
        return `🕛 ${days} day${days > 1 ? "s" : ""} ago`;
    } else if (diffInSeconds < 2592000) {
        const weeks = Math.floor(diffInSeconds / 604800);
        return `🕛 ${weeks} week${weeks > 1 ? "s" : ""} ago`;
    } else if (diffInSeconds < 31536000) {
        const months = Math.floor(diffInSeconds / 2592000);
        return `🕛 ${months} month${months > 1 ? "s" : ""} ago`;
    } else {
        const years = Math.floor(diffInSeconds / 31536000);
        return `🕛 ${years} year${years > 1 ? "s" : ""} ago`;
    }
};