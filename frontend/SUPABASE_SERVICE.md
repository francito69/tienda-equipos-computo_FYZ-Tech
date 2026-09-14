# Supabase Service - Angular

## Setup

1. **Install dependencies**:
   ```bash
   npm install
   ```

2. **Environment configuration**:
   The Supabase credentials are already configured in `src/environment.ts`:
   - URL: `https://kkkffeotqqbtjgtsphnb.supabase.co`
   - Bucket: `imgs`

## Usage

### Import the service

```typescript
import { SupabaseService } from './services/supabase.service';

constructor(private supabaseService: SupabaseService) {}
```

### Upload an image

```typescript
async uploadProductImage(file: File) {
  try {
    const imageUrl = await this.supabaseService.uploadImage(file, 'imgs');
    console.log('Image uploaded:', imageUrl);
    return imageUrl;
  } catch (error) {
    console.error('Upload failed:', error);
  }
}
```

### Delete an image

```typescript
async deleteProductImage(fileName: string) {
  try {
    await this.supabaseService.deleteImage(fileName, 'imgs');
    console.log('Image deleted');
  } catch (error) {
    console.error('Delete failed:', error);
  }
}
```

### Example: File input handler

```typescript
onFileSelected(event: Event) {
  const input = event.target as HTMLInputElement;
  if (input.files && input.files[0]) {
    const file = input.files[0];
    
    // Upload to Supabase
    this.supabaseService.uploadImage(file)
      .then(url => {
        console.log('Image URL:', url);
        // Use this URL for your product imgUrl field
      })
      .catch(error => console.error(error));
  }
}
```

## Service Methods

### `uploadImage(file: File, bucket?: string): Promise<string>`
- Uploads a file to Supabase Storage
- Returns the public URL of the uploaded image
- Default bucket: `imgs`

### `deleteImage(fileName: string, bucket?: string): Promise<void>`
- Deletes a file from Supabase Storage
- Default bucket: `imgs`

### `getClient(): SupabaseClient`
- Returns the Supabase client instance for advanced operations

## Notes

- Files are automatically renamed with timestamp prefix to avoid conflicts
- All methods are async and return Promises
- Errors are logged to console and re-thrown for handling
