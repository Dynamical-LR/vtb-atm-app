use serde::Deserialize;
use validator::Validate;

#[derive(Debug, Deserialize, Validate)]
pub struct PageRequest {
    #[validate(range(min = 1, max = 9999))]
    pub page: usize,
    pub size: usize,
}

impl PageRequest {
    pub fn paginate<I, T>(&self, iter: I) -> Vec<T>
    where
        I: IntoIterator<Item = T>,
    {
        iter.into_iter()
            .skip(self.size * (self.page - 1))
            .take(self.size)
            .collect::<Vec<_>>()
    }
}
