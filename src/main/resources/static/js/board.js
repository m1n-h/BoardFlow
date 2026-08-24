export async function fetchArticles() {
    const response = await fetch("/api/articles");
    if (!response.ok)
        return [];
    return await response.json();
}
export async function createArticle(title, content) {
    const response = await fetch("/api/articles", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ title, content })
    });
    return response.ok;
}
