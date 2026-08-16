export interface Article {
    id: number;
    title: string;
    content: string;
}

export async function fetchArticles(): Promise<Article[]> {
    const response = await fetch("/api/articles");
    if (!response.ok) return [];

    return await response.json();
}

export async function createArticle(title: string, content: string): Promise<boolean> {
    const response = await fetch("/api/articles", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({title, content})
    });

    return response.ok;
}